package edu.eci.arsw.moneylaundering;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoneyLaundering extends Thread {
    private TransactionAnalyzer transactionAnalyzer;
    private TransactionReader transactionReader;
    private int amountOfFilesTotal;
    private AtomicInteger amountOfFilesProcessed;
    private List<File> files;

    private boolean pause = false;

    @Override
    public void run() {
        processTransactionData(this.files);
    }

    public MoneyLaundering(List<File> files) {
        transactionAnalyzer = new TransactionAnalyzer();
        transactionReader = new TransactionReader();
        amountOfFilesProcessed = new AtomicInteger();
        this.files = files;
    }

    public void processTransactionData(List<File> files) {
        amountOfFilesProcessed.set(0);
        List<File> transactionFiles = files;
        amountOfFilesTotal = transactionFiles.size();
        for (File transactionFile : transactionFiles) {
            List<Transaction> transactions = transactionReader.readTransactionsFromFile(transactionFile);
            for (Transaction transaction : transactions) {
                transactionAnalyzer.addTransaction(transaction);
            }
            amountOfFilesProcessed.incrementAndGet();
        }
        while (pause) {
            try {
                wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public List<String> getOffendingAccounts() {
        return transactionAnalyzer.listOffendingAccounts();
    }

    private static List<File> getTransactionFileList() {
        List<File> csvFiles = new ArrayList<>();
        try (Stream<Path> csvFilePaths = Files.walk(Paths.get("src/main/resources/"))
                .filter(path -> path.getFileName().toString().endsWith(".csv"))) {
            csvFiles = csvFilePaths.map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFiles;
    }

    public static void main(String[] args) {
        System.out.println(getBanner());
        System.out.println(getHelp());

        List<MoneyLaundering> threads = new ArrayList<>();

        int numThreads = 5;

        int inicio = 0;
        int interval = (int) MoneyLaundering.getTransactionFileList().size() / 5;
        List<File> fi = MoneyLaundering.getTransactionFileList();
        int residuo = 0;
        if (interval % numThreads != 0) {
            residuo = interval % numThreads;
        }
        int fin = interval;
        if (residuo == 0) {
            for (int i = 0; i < numThreads; i++) {
                List<File> files = new ArrayList<>();
                for (int j = inicio; i < fin; i++) {
                    files.add(fi.get(j));
                }
                threads.add(new MoneyLaundering(files));
                files = new ArrayList<>();
                inicio += interval;
                fin += interval;
            }
        } else {
            List<File> files = new ArrayList<>();
            for (int i = 0; i < numThreads - 1; i++) {
                for (int j = inicio; j < fin; j++) {
                    files.add(fi.get(j));
                }
                threads.add(new MoneyLaundering(files));
                files = new ArrayList<>();
                inicio += interval;
                fin += interval;
            }
            for (int j = inicio; j < fin; j++) {
                files.add(fi.get(j));
            }
            threads.add(new MoneyLaundering(files));
            files = new ArrayList<>();
            inicio += interval;
            fin += interval;
        }

        threads.forEach(t -> t.start());

        // MoneyLaundering moneyLaundering = new MoneyLaundering();
        // Thread processingThread = new Thread(() ->
        // moneyLaundering.processTransactionData());
        // processingThread.start();
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if (line.contains("exit")) {
                System.exit(0);
            }

            threads.forEach(t-> t.pausar());

            for (MoneyLaundering ml : threads) {

                String message = "Processed %d out of %d files.\nFound %d suspect accounts:\n%s";
                List<String> offendingAccounts = ml.getOffendingAccounts();
                String suspectAccounts = offendingAccounts.stream().reduce("", (s1, s2) -> s1 + "\n" + s2);
                message = String.format(message, ml.amountOfFilesProcessed.get(), ml.amountOfFilesTotal,
                        offendingAccounts.size(), suspectAccounts);
                System.out.println(message);
            }

            line = scanner.nextLine();

            threads.forEach(t->t.continuar());

        }
    }

    private static String getBanner() {
        String banner = "\n";
        try {
            banner = String.join("\n", Files.readAllLines(Paths.get("src/main/resources/banner.ascii")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return banner;
    }

    private static String getHelp() {
        String help = "Type 'exit' to exit the program. Press 'Enter' to get a status update\n";
        return help;
    }

    public void continuar(){
        this.pause = false;
        synchronized(this){
            notify();
        }
    }

    public void pausar(){
        this.pause=true;
    }
}