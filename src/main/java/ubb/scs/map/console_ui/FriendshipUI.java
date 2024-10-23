package ubb.scs.map.console_ui;


import ubb.scs.map.service.FriendshipCrudService;

import java.util.Scanner;

public class FriendshipUI {
    private FriendshipCrudService service;

    // fara modificatori de acces ca sa poata fi accesat doar de catre pachet (adica doar de ConsoleUI)
    FriendshipUI(FriendshipCrudService service) {
        this.service = service;
    }

    private void printMenu() {
        String main_menu = """
                1. Adauga prietenie
                2. Sterge prietenie
                0. Inapoi
                """;
        System.out.println(main_menu);
    }

    public void run() {
        printMenu();
        Scanner input = new Scanner(System.in);
        while(true) {
            System.out.print("Introdu optiunea: ");
            int option;
            try {
                option = input.nextInt();
            } catch (Exception e) {
                System.out.println("Optiune invalida");
                input.nextLine();
                continue;
            }
            switch (option) {
                case 1:
                    addFriendship();
                    printMenu();
                    break;
                case 2:
                    deleteFriendship();
                    printMenu();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Optiune invalida");
            }
        }
    }

    private void addFriendship() {
        Scanner input = new Scanner(System.in);
        System.out.print("Introdu id-ul primului utilizator: ");
        long id1, id2;
        try {
            id1 = input.nextLong();
        } catch (Exception e) {
            System.out.println("Numar invalid");
            input.nextLine();
            return;
        }
        System.out.print("Introdu id-ul celui de-al doilea utilizator: ");
        try {
            id2 = input.nextLong();
        } catch (Exception e) {
            System.out.println("Numar invalid");
            input.nextLine();
            return;
        }

        try {
            var result = service.add(id1, id2);
            if (result != null) {
                throw new Exception("Prietenia nu a putut fi adaugata (id-ul deja exista");
            }
            System.out.println("Prietenie adaugata cu succes");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteFriendship() {
        Scanner input = new Scanner(System.in);
        System.out.print("Introdu id-ul prieteniei: ");
        long id;
        try {
            id = input.nextLong();
        } catch (Exception e) {
            System.out.println("Numar invalid");
            input.nextLine();
            return;
        }

        try {
            var result = service.delete(id);
            if (result == null) {
                throw new Exception("Prietenia nu a putut fi stearsa (id-ul nu exista)");
            }
            System.out.println("Prietenia a fost stearsa cu succes");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
