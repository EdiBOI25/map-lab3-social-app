package ubb.scs.map.console_ui;

import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.repository.Repository;

import java.util.Scanner;

public class ConsoleUI {
    private final String main_menu = "----------------------------\n" +
            "MENIU PRINCIPAL\n" +
            "----------------------------\n" +
            "1. Utilizatori\n" +
            "2. Prietenii\n" +
            "0. Exit\n";
    private Repository<Long, Utilizator> repo_users;
//    private Repository<Long, Prietenie> repo_friends;

    public ConsoleUI(Repository<Long, Utilizator> repo_users) {
        this.repo_users = repo_users;
    }

    private void showMainMenu() {
        System.out.println(main_menu);
    }

    public void run() {
        showMainMenu();
        Scanner input = new Scanner(System.in);
        while(true) {
            System.out.print("Introduceti optiunea: ");
            int option = input.nextInt();
            switch (option) {
                case 1:
                    System.out.println("Utilizatori");
                    break;
                case 2:
                    System.out.println("Prietenii");
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Optiune invalida");
            }
        }
    }
}
