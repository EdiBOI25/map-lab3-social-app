package ubb.scs.map.console_ui;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.repository.Repository;

import java.util.Scanner;

public class ConsoleUI{
    protected Repository<Long, Utilizator> repo_users;
    protected Repository<Long, Prietenie> repo_friendships;
    private UserUI userUI;
    private FriendshipUI friendshipUI;
    private CommunitiesUI communitiesUI;

    public ConsoleUI(Repository<Long, Utilizator> repo_users, Repository<Long, Prietenie> repo_friendships) {
        this.repo_users = repo_users;
        this.repo_friendships = repo_friendships;
        userUI = new UserUI(repo_users);
        friendshipUI = new FriendshipUI(repo_friendships);
        communitiesUI = new CommunitiesUI(repo_users, repo_friendships);
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
                    userUI.run();
                    printMenu();
                    break;
                case 2:
                    friendshipUI.run();
                    printMenu();
                    break;
                case 3:
                    communitiesUI.run();
                    printMenu();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Optiune invalida");
            }
        }
    }
}
