package ubb.scs.map.console_ui;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.service.*;

import java.util.Scanner;

public class ConsoleUI{
    protected CrudService<Long, Utilizator> user_service;
    protected CrudService<Long, Prietenie> friendship_service;
    protected CommunityService community_service;
    private UserUI userUI;
    private FriendshipUI friendshipUI;
    private CommunitiesUI communitiesUI;

    public ConsoleUI(CrudService<Long, Utilizator> user_service,
                     CrudService<Long, Prietenie> friendship_service,
                     CommunityService community_service) {
        this.user_service = user_service;
        this.friendship_service = friendship_service;
        this.community_service = community_service;
        userUI = new UserUI((UserCrudService) user_service);
        friendshipUI = new FriendshipUI((FriendshipCrudService) friendship_service);
        communitiesUI = new CommunitiesUI(community_service);
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
