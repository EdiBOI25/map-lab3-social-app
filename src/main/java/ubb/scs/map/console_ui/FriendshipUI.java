package ubb.scs.map.console_ui;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.repository.Repository;

import java.util.Scanner;

public class FriendshipUI {
    private Repository<Long, Prietenie> repo_friendships;

    // fara modificatori de acces ca sa poata fi accesat doar de catre pachet (adica doar de ConsoleUI)
    FriendshipUI(Repository<Long, Prietenie> repo_friendships) {
        this.repo_friendships = repo_friendships;
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
            int option = input.nextInt();
            switch (option) {
                case 1:
                    System.out.println("Adding friendship\n");
                    break;
                case 2:
                    System.out.println("Deleting friendship\n");
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Optiune invalida");
            }
        }
    }
}
