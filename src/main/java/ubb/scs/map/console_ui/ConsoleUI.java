package ubb.scs.map.console_ui;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.repository.Repository;

import java.util.Scanner;

public class ConsoleUI{
    private Repository<Long, Utilizator> repo_users;
    private Repository<Long, Prietenie> repo_friendships;

    public ConsoleUI(Repository<Long, Utilizator> repo_users, Repository<Long, Prietenie> repo_friendships) {
        this.repo_users = repo_users;
        this.repo_friendships = repo_friendships;
    }

    private void printMenu() {
        String main_menu = """
                ----------------------------
                MENIU PRINCIPAL
                ----------------------------
                1. Meniu utilizatori
                2. Meniu prietenii
                3. Meniu comunitati
                0. Exit
                """;
        System.out.println(main_menu);
    }

    public void run() {
        printMenu();
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
                case 3:
                    System.out.println("Comunitati");
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Optiune invalida");
            }
        }
    }
}
