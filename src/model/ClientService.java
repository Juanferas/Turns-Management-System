package model;
import java.util.*;
import customExceptions.*;

public class ClientService {

    private char letter;
	private int num;
    private ArrayList<User> users;
	private ArrayList<Turn> actualTurns;
	private ArrayList<Turn> attendedTurns;
	private ArrayList<Turn> notAttendedTurns;
    
    public ClientService() {
        letter = 'A';
		num = -1;
		users = new ArrayList<User>();
		actualTurns = new ArrayList<Turn>();
		attendedTurns = new ArrayList<Turn>();
		notAttendedTurns = new ArrayList<Turn>();
    }
    	
    /**
	 * @return the letter
	 */
	public char getLetter() {
		return letter;
	}

	/**
	 * @param letter the letter to set
	 */
	public void setLetter(char letter) {
		this.letter = letter;
	}

	/**
	 * @return the number
	 */
	public int getNum() {
		return num;
	}

	/**
	 * @param num the number to set
	 */
	public void setNum(int num) {
		this.num = num;
	}

	/**
	 * @return the users
	 */
	public ArrayList<User> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	/**
	 * @return the actualTurns
	 */
	public ArrayList<Turn> getActualTurns() {
		return actualTurns;
	}

	/**
	 * @param actualTurns the actualTurns to set
	 */
	public void setActualTurns(ArrayList<Turn> actualTurns) {
		this.actualTurns = actualTurns;
	}

	/**
	 * @return the attendedTurns
	 */
	public ArrayList<Turn> getAttendedTurns() {
		return attendedTurns;
	}

	/**
	 * @param attendedTurns the attendedTurns to set
	 */
	public void setAttendedTurns(ArrayList<Turn> attendedTurns) {
		this.attendedTurns = attendedTurns;
	}

	/**
	 * @return the notAttendedTurns
	 */
	public ArrayList<Turn> getNotAttendedTurns() {
		return notAttendedTurns;
	}

	/**
	 * @param notAttendedTurns the notAttendedTurns to set
	 */
	public void setNotAttendedTurns(ArrayList<Turn> notAttendedTurns) {
		this.notAttendedTurns = notAttendedTurns;
	}

	public String verifyID(String documentNumber) throws RepeatedDocumentException{
        for (int i = 0; i<users.size(); i++) {
			if (users.get(i).getDocumentNumber().equals(documentNumber)) {
				throw new RepeatedDocumentException(documentNumber);
			}
		}
		return "ok";
    }

    public void registerUser(String documentType, String documentNumber, String name, String lastNames, String phone, String address) {
        if (phone.equals(""))
            phone = "Not given";
        if (address.equals(""))
            address = "Not given";
        users.add(new User(documentType, documentNumber, name, lastNames, phone, address));
    }

	public String findUser(String id) throws UserNotFoundException{
		String userFound = "";
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getDocumentNumber().equals(id)) {
				userFound = "\n-------USER-------\n"+users.get(i).toString();
			}
		}
		if (userFound.equals("")) {
			throw new UserNotFoundException(id);
		}
		else {
			return userFound;
		}
	}

    public String assignTurn(String id) throws UserAlreadyHasTurnException{
		String turn = "";
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getDocumentNumber().equals(id)) {
				if (users.get(i).hasTurn()) {
					throw new UserAlreadyHasTurnException(id, users.get(i).getAssignedTurn());
				}
				else {
					turn = nextTurn();
					users.get(i).assignTurn(turn);
					actualTurns.add(new Turn(turn, users.get(i)));
				}
			}
		}
		return "<<Turn assigned correctly ["+turn+"]>>";
	}

	public String nextTurn() {
		String turn = "";
		num ++;
		if (num==100 && letter!='Z') {
			letter += 1;
			num = 0;
			turn = letter+"0"+String.valueOf(num);
		}
		else if (num==100 && letter=='Z') {
			letter = 'A';
			num = 0;
			turn = letter+"0"+String.valueOf(num);
		}
		else if (num>10) {
			turn = letter+String.valueOf(num);
		}
		else {
			turn = letter+"0"+String.valueOf(num);
		}
		return turn;
	}
	
	public String getActualTurn() {
		String turnID = "";
		if (actualTurns.size()==0) {
			turnID = "<<There are no turns to attend>>";
		}
		else {
			turnID = actualTurns.get(0).getTurnID();
			notAttendedTurns.add(actualTurns.get(0));
			actualTurns.remove(0);
		}
		return turnID;
	}
	
	public void endTurn(int op, String turnID) {
		Turn pturn = null;
		for (int i=0; i<notAttendedTurns.size(); i++) {
			if (notAttendedTurns.get(i).getTurnID()==(turnID)) {
				notAttendedTurns.get(i).setInUse(false);
				notAttendedTurns.get(i).getUser().setHasTurn(false);
				notAttendedTurns.get(i).getUser().setAssignedTurn("");
				pturn = notAttendedTurns.get(i);
				break;
			}
		}
		if (op==1) {
			notAttendedTurns.remove(pturn);
			attendedTurns.add(pturn);
		}
	}
}