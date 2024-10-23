package ubb.scs.map.console_ui;

import ubb.scs.map.service.UserCrudService;

import java.util.Scanner;

public class UserUI {
    private UserCrudService service;

    // fara modificatori de acces ca sa poata fi accesat doar de catre pachet (adica doar de ConsoleUI)
    UserUI(UserCrudService service) {
        this.service = service;
    }

    private void printMenu() {
        String main_menu = """
                1. Adauga utilizator
                2. Sterge utilizator
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
                    addUser();
                    printMenu();
                    break;
                case 2:
                    deleteUser();
                    printMenu();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Optiune invalida");
            }
        }
    }

    private void addUser() {
        Scanner input = new Scanner(System.in);
        System.out.print("Introdu prenumele utilizatorului: ");
        String first_name = input.nextLine();
        System.out.print("Introdu numele de familie al utilizatorului: ");
        String last_name = input.nextLine();

        try {
            var result = service.add(first_name, last_name);
            if (result != null) {
                throw new Exception("Utilizatorul nu a putut fi adaugat (id-ul deja exista");
            }
            System.out.println("Utilizator adaugat cu succes");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteUser() {
        Scanner input = new Scanner(System.in);
        System.out.print("Introdu id-ul utilizatorului: ");
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
                throw new Exception("Utilizatorul nu a putut fi sters (id-ul nu exista)");
            }
            System.out.println("Utilizator sters cu succes");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
