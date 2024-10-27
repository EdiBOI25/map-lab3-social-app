package ubb.scs.map.console_ui;

import ubb.scs.map.service.CommunityService;

import java.util.Scanner;

public class CommunitiesUI {
    private CommunityService service;

    // fara modificatori de acces ca sa poata fi accesat doar de catre pachet (adica doar de ConsoleUI)
    CommunitiesUI(CommunityService service) {
        this.service = service;
    }

    private void printMenu() {
        String main_menu = """
                1. Afiseaza numar comunitati
                2. Afiseaza cea mai mare comunitate
                0. Inapoi
                """;
        System.out.println(main_menu);
    }

    public void run() {
        printMenu();
        Scanner input = new Scanner(System.in);
        while(true) {
            System.out.print("Introdu optiunea: ");
            int option = input.nextInt();
            switch (option) {
                case 1:
                    printCommunities();
                    printMenu();
                    break;
                case 2:
                    printBiggestCommunity();
                    printMenu();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Optiune invalida");
            }
        }
    }

    private void printCommunities() {
        try {
            System.out.println("Number of communities: " + service.numberOfCommunities());
        } catch (Exception e) {
            System.out.println("0. " + e.getMessage());
        }
    }

    private void printBiggestCommunity() {
        try {
            final var list = service.biggestCommunity();
            list.forEach(System.out::println);
            System.out.println();
        } catch (Exception e) {
            System.out.println("Nu exista. " + e.getMessage());
        }
    }
}
