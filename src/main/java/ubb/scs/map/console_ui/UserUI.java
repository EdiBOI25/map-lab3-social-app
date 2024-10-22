package ubb.scs.map.console_ui;

import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.repository.Repository;

import java.util.Scanner;

public class UserUI {
    private Repository<Long, Utilizator> repo_users;

    // fara modificatori de acces ca sa poata fi accesat doar de catre pachet (adica doar de ConsoleUI)
    UserUI(Repository<Long, Utilizator> repo_users) {
        this.repo_users = repo_users;
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
            int option = input.nextInt();
            switch (option) {
                case 1:
                    System.out.println("Adding user\n");
                    break;
                case 2:
                    System.out.println("Deleting user\n");
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Optiune invalida");
            }
        }
    }
}
