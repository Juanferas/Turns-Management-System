package ui;
import model.*;
import java.util.*;
import customExceptions.*;

public class Main {

	private Scanner sc;
	private ClientService service;

	public Main() {
		sc = new Scanner(System.in);
		service = new ClientService();
	}

	public static void main(String[] args) {

		Main obj = new Main();
		boolean endWhile = false;

		while (!endWhile) {
			try {
				while (!endWhile) {
					System.out.println("\n-------MENU-------\n[1] Add User.\n[2] Register turn.\n[3] Attend turn.\n[4] Exit.");
					int op = Integer.valueOf(obj.sc.nextLine());
					switch (op) {
						case 1:
							obj.registerUser();
							break;
						case 2:
							obj.registerTurn();
							break;
						case 3:
							obj.attendTurn();
							break;
						case 4:
							endWhile = true;
							break;
					}
				}
			} catch (NumberFormatException nfe) {
				System.out.println("<<Invalid input. Please try again>>");
			}
		}
	}

	/**
	 * This method asks for the attributes of an user in order to send them to the "registerUser" method in ClientService class.
	 * <b>pre:</b> An object of type Main has already been created, as well as the "registerUser" method in ClientService class.
     * <b>post:</b> The information typed by the user has been correctly passed to the method in ClientService class.
	 */
	public void registerUser() {
		System.out.println("\nPlease fill in the next form, the fields marked with an asterisk (*) are mandatory.\n");
		System.out.println("Document type*:\n[1] Citizenship card.\n[2] Identity card.\n[3] Civil registration.\n[4] Passport.\n[5] Foreign identity card.");
		String documentType = "";
		while (true) {
			try {
				String[] documentTypes = {"Citizenship card", "Identity card", "Civil registration", "Passport", "Foreign identity card"};
				documentType = documentTypes[Integer.valueOf(sc.nextLine())];
				break;	
			} catch (Exception e) {
				System.out.println("<<Invald input. Please try again>>");
			}
		}
		String documentNumber = "";
		while (documentNumber.equals("")) {
			System.out.print("Document number*: ");
			documentNumber = sc.nextLine();
			if (documentNumber.equals("")) {
				System.out.println("<<You can't leave this field empty. Please try again>>");
			}
		}
		try {
			service.verifyID(documentNumber);
		} catch (RepeatedDocumentException rde) {
			System.out.println(rde.getMessage());
			System.out.println("<<User couldn't be registered>>");
			return;
		}
		String name = "";
		while (name.equals("")) {
			System.out.print("First Name*: ");
			name = sc.nextLine();
			if (name.equals("")) {
				System.out.println("<<You can't leave this field empty. Please try again>>");
			}
		}
		String lastNames = "";
		while (lastNames.equals("")) {
			System.out.print("Last names*: ");
			lastNames = sc.nextLine();
			if (lastNames.equals("")) {
				System.out.println("<<You can't leave this field empty. Please try again>>");
			}
		}
		System.out.print("Phone: ");
		String phone = sc.nextLine();
		System.out.print("Address: ");
		String address = sc.nextLine();
		service.registerUser(documentType, documentNumber, name, lastNames, phone, address);
		System.out.println("<<User registered succesfully>>");
	}

	/**
	 * This method ask for the document number of an user in order to register him a turn.
	 * <b>pre:</b> An object of type Main has already been created, as well as the "assignTurn" method in ClientService class.
     * <b>post:</b> A turn has been assigned to the user with the entered document number.
	 */
	public void registerTurn () {
		System.out.print("\nDocument number: ");
		String documentNumber = sc.nextLine();
		try {
			System.out.println(service.findUser(documentNumber));
			int op = 0;
			while (true) {
				try {
					System.out.println("\n[1] Register turn.\n[2] Go back.");
					op = Integer.valueOf(sc.nextLine());
					break;
				} catch (NumberFormatException nfe) {
					System.out.println("<<Invalid Input. Please try Again>>");
				}
			}
			if (op==1) {
				System.out.println(service.assignTurn(documentNumber));
			}
		}
		catch (UserNotFoundException unfe) {
			System.out.println(unfe.getMessage());
		}
		catch (UserAlreadyHasTurnException uahte) {
			System.out.println(uahte.getMessage());
		}
	}
	
	/**
	 * This method gets the actual turn and asks for what to do with it.
	 * <b>pre:</b> An object of type Main has already been created, as well as the "endTurn" method in ClientService class.
     * <b>post:</b> The option selected by the user works correctly when executed in the method in ClientService class.
	 */
	public void attendTurn() {
		String turnID = service.getActualTurn();
		if (turnID.length()>3) {
			System.out.println(turnID);
		}
		else {
			while (true) {
				try {
					System.out.println("\nActual turn: "+turnID+"\n[1]Turn attended.\n[2]User not present.");
					int op = Integer.valueOf(sc.nextLine());
					service.endTurn(op, turnID);
					break;
				} catch (NumberFormatException nfe) {
					System.out.println("<<Invalid Input. Please try Again>>");
				}
			}
		}
	}
}