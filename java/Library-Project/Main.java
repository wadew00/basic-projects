import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

class LibraryDBMS {
    protected  ArrayList<LibraryItems> libItems = new ArrayList<>();
    protected   ArrayList<Users> users = new ArrayList<>();
    protected final int PENALTY_COST = 2;
    //Register actions

    public  void addItem(LibraryItems item){
        libItems.add(item);
    }
    public  void addUser(Users user) {
        users.add(user);
    }
    public  void displayItems() {
        libItems.sort(Comparator.comparingInt(LibraryItems::getID));
        for (LibraryItems item: libItems){
            System.out.println("\n\n"+"-".repeat(6)+ " Item Information for "+item.id+ " "+ "-".repeat(6));
            System.out.println(item);
        }
    }
    public  void displayUsers() {
        users.sort(Comparator.comparingInt(Users::getID));
        System.err.println("\n\n");

        for (Users user : users) {
            System.out.println("\n\n"+"-".repeat(6)+ " User Information for "+user.id+ " "+ "-".repeat(6));
            System.out.println(user);
        }
     }

    public  void registerItems(String fName) {
        String line;
        try (BufferedReader bf = new BufferedReader(new FileReader(fName))){
            while ((line=bf.readLine())!= null) {
                String[] itemElements = line.split(",");
                switch (itemElements[0]) {
                    case "B":
                        String bookID= itemElements[1];
                        String bookName= itemElements[2];
                        String bookAuthor= itemElements[3];
                        String bookGenre= itemElements[4];
                        String bookType= itemElements[5];
                        addItem(new Book(bookID, bookName, bookAuthor, bookGenre, bookType));
                        break;
                    case "M":
                        String magazineID= itemElements[1];
                        String magazineName= itemElements[2];
                        String magazinePublisher= itemElements[3];
                        String magazineCategory= itemElements[4];
                        String magazineType= itemElements[5];
                        addItem(new Magazine(
                                             magazineID, magazineName,
                                             magazinePublisher, magazineCategory, magazineType));
                        break;
                    case "D" :
                        String dvdID= itemElements[1];
                        String dvdName= itemElements[2];
                        String dvdDirectory= itemElements[3];
                        String dvdCategory= itemElements[4];
                        String dvdRuntime= itemElements[5];
                        String dvdType= itemElements[6];
                        addItem(new DVD(dvdID, dvdName, dvdDirectory, dvdCategory, dvdRuntime, dvdType));
                        break;
                }
            }
        } catch (FileNotFoundException e ) {
            System.err.println("Error: The file '" + fName + "' does not exist. Please check the file path.");
        }   catch (IOException e ) {
            System.err.println("Error: Unable to read the file '" + fName + "'. Reason: " + e.getMessage());
        }
    }



    public  void registerUsers(String fName) {
        String line;
        try (BufferedReader bf = new BufferedReader(new FileReader(fName))){
            while ((line=bf.readLine())!= null) {
                String[] userElements = line.split(",");
                switch (userElements[0]) {
                    case "S":
                        String name= userElements[1];
                        String id= userElements[2];
                        String pNumber= userElements[3];
                        String department= userElements[4];
                        String faculty= userElements[5];
                        String grade = userElements[6];
                        addUser(new Student(name, id, pNumber, department, faculty, grade));
                        break;
                    case "A":
                        String aName= userElements[1];
                        String aId= userElements[2];
                        String aPNumber= userElements[3];
                        String aDepartment= userElements[4];
                        String aFaculty= userElements[5];
                        String aTitle = userElements[6];
                        addUser(new AcademicStaff(aName, aId, aPNumber, aDepartment, aFaculty, aTitle));
                        break;
                    case "G" :
                        String gName= userElements[1];
                        String gID= userElements[2];
                        String gPNumber = userElements[3];
                        String gOccupatiton= userElements[4];
                        addUser(new Guest(gName, gID, gPNumber, gOccupatiton));
                        break;
                }
            }
        } catch (FileNotFoundException e ) {
            System.err.println("Error: The file '" + fName + "' does not exist. Please check the file path.");
        }   catch (IOException e ) {
            // TODO Auto-generated catch block
            System.err.println("Error: Unable to read the file '" + fName + "'. Reason: " + e.getMessage());
        }
    }
    private void updateItems(ArrayList<LibraryItems> items,LocalDate date) {
        for (LibraryItems item: items) {
            if (!item.available && item.overdueDate.isBefore(date)) {
                
                item.available= true;
                item.borrowedBy.penalty+=PENALTY_COST;


            }
        }

    }
    public  void commandExecuter(String fName) {
        String line;
        LibraryItems item = null;
        Users foundedUser = null;
        String[] commandElements;
        String command;
        try (BufferedReader bf = new BufferedReader(new FileReader(fName))){
            while ((line=bf.readLine())!= null) {
                commandElements = line.split(",");
                
                if (commandElements.length>1) {
                    command= commandElements[0];
                    foundedUser= idFinder(commandElements[1]);  
                    if(commandElements.length>2) {
                        String itemID = commandElements[2];
                        item = itemFounder(libItems, itemID);
                        if (item == null) {
                            System.err.println("Item " + itemID + " not found!");
                            continue; // Skip to the next command
                    }
                  } 
                }else {
                    command= line;
                }


                switch (command){
                    case ("borrow"):
                        String sBorrowDate= commandElements[3];
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate borrowDate = LocalDate.parse(sBorrowDate,formatter);
                        updateItems(libItems,borrowDate);
                        foundedUser.borrow(item,borrowDate);
                        break;
                    case ("return") :
                        String itemToReturnID = commandElements[2];
                        item = itemFounder(libItems, itemToReturnID);
                        foundedUser.returnItem(item);
                        break;
                    case ("displayUsers") :
                        displayUsers();
                        break;
                    case ("displayItems") :
                        displayItems();
                        break;
                    case ("pay"):
                        foundedUser.pay();
                        break;
                    
                }
            }
          
        } catch (IOException ex) {
         System.err.println("File doesn't open correctly or does not exist");
          }
    }
    private LibraryItems itemFounder(ArrayList<LibraryItems> lib,String itemID) {
        for (LibraryItems item: lib) {
            if (item.id.equals(itemID)){
                return item;
            }
        }
        return null;
    }

    private Users idFinder(String id) {
        for (Users user: users) {
            if (id.equals(user.id)) {
                return user;
            }
        }
        return null;
    }
}
class LibraryItems{
    String id;
    String name;
    String type;
    boolean available = true;
    LocalDate borrowedDate;
    Users borrowedBy;
    LocalDate overdueDate;

    @Override
    public String toString() {
        String status =  available ? "Available" : "Borrowed"; 
        String build = "ID: "+id+" Name: "+ name + " Status: " +status;
        if (!available) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedBorrowDate = borrowedDate.format(formatter);
            build=  build + " Borrowed Date: " + formattedBorrowDate + " Borrowed by: " + borrowedBy.name;
        }

        return build;
    }
    public int getID() {
        return Integer.parseInt(id);
    }


    
}
class Book extends LibraryItems{
    String author;
    String genre;
    Book(String bookID, String bookName,String bookAuthor,String bookGenre,String bookType) {
        this.id= bookID;
        this.name=bookName;
        this.author=bookAuthor;
        this.genre=bookGenre;
        this.type=bookType;
        
    }

    @Override
    public String toString() {
        return super.toString() + "\nAuthor: " + author + " Genre: " + genre;
    }
    

}
class Magazine extends LibraryItems{
    String publisher;
    String category;

    Magazine(String magazineID, String magazineName,String magazinePublisher,String magazineCategory,String magazineType) {
        this.id=magazineID;
        this.name=magazineName;
        this.publisher=magazinePublisher;
        this.category=magazineCategory;
        this.type=magazineType;
    }

    @Override
    public String toString() {
        return super.toString() + "\nPublisher: " + publisher + " Category: " + category;
    }
    
    
}
class DVD extends  LibraryItems {
    String  directory,category,runtime;

    public DVD(String dvdID, String dvdName,String dvdDirectory,String dvdCategory,String dvdRuntime, String dvdType) {
        this.id=dvdID;
        this.name=dvdName;
        this.directory=dvdDirectory;
        this.category=dvdCategory;
        this.runtime=dvdRuntime;
        this.type=dvdType;
    }

    @Override
    public String toString() {
         return super.toString() + "\nDirector: " + directory + " Category: " + category + "Runtime: " + runtime;
    }
    
}
abstract class Users {
    final int  PENALTY_THRESHOLD = 6;
    String id;
    String name;
    String pNumber;
    int borrowedItemsCount = 0;
    int penalty = 0;
    ArrayList<String> borrowedItemsIDs= new ArrayList<>();

    @Override
    public String toString() {
        return "Name: " + name + " Phone: " + pNumber;
    }
    abstract void borrow(LibraryItems item, LocalDate borrowDate);
    abstract void returnItem(LibraryItems item);

    protected void pay(){
        penalty= 0;
    }
    public int getID() {
        return Integer.parseInt(id);
    }

}
class Student extends Users {
    final int  MAX_ITEMS = 5;
    final int  OVER_DUE = 30;
    final String RESTRICTED_ITEMS = "reference";
    String faculty;
    String department;
    String grade;

    Student(String name,String id,String pNumber,String department,String faculty,String grade) {
        this.id= id;
        this.name = name;
        this.pNumber=pNumber;
        this.department= department;
        this.faculty= faculty;
        this.grade= grade;
    }

    @Override
    public String toString() {
        return  super.toString() + "\nFaculty: "+ faculty + " Department: " + department + " Grade: "+ grade+"th" ;
    }

    @Override
    public void borrow(LibraryItems item,LocalDate borrowDate) {
       
 
        boolean isBorrowable = borrowableCheck(item);
        if  (!isBorrowable) {
            return;
        }
        else { 
            
            borrowedItemsCount+=1;
            borrowedItemsIDs.add(item.id);
            item.available= false;
            item.borrowedBy = this;
            item.borrowedDate= borrowDate;
            item.overdueDate = borrowDate.plusDays(OVER_DUE);
            System.out.println(name+" successfully borrowed! " + item.name);
        }
        
    }
    private boolean borrowableCheck(LibraryItems item) {
  
        
        if (borrowedItemsCount>= MAX_ITEMS) {
            System.err.println(name +" cannot borrow " + item.name +", since the borrow limit has been reached!");
            return false;
        } else if ( ! item.available) {
            System.err.println(name +" cannot borrow " + item.name +", it is not available!");
            return false;
        } else if (penalty>=PENALTY_THRESHOLD) {
            System.err.printf(name +" cannot borrow " + item.name +", you must first pay the penalty amount! %s$\n",PENALTY_THRESHOLD);
            return false;
        } else if (item.type.equals(RESTRICTED_ITEMS)) {
            System.err.println(name +" cannot borrow " + item.type +" item!");
            return false;
        }
        return true;
    }

    @Override
    void returnItem(LibraryItems item) {
        item.available= true;
        borrowedItemsCount-=1;
        borrowedItemsIDs.remove(item.id);
        System.out.println(name+ " successfully returned " + item.name);
    }


    
}

class AcademicStaff extends Users {
    final int  MAX_ITEMS = 3;
    final int  OVER_DUE = 15;
    // Academic Staff do have all of the item types. So no need for adding restricted items.
    String faculty, department ,title;

    AcademicStaff(String name,String id,String pNumber,String department,String faculty,String title) {
        this.name=name;
        this.id=id;
        this.pNumber=pNumber;
        this.department=department;
        this.faculty=faculty;
        this.title=title;

    }

    @Override
    public String toString() {
        return  "Name: "+ title+ " "+ name +" Phone: "+ pNumber+ "\nFaculty: "+ faculty + " Department: " + department;
    }

    @Override
    public void borrow(LibraryItems item,LocalDate borrowDate) {

        boolean isBorrowable = borrowableCheck(item);
        if  (isBorrowable) {
            borrowedItemsCount+=1;
            borrowedItemsIDs.add(item.id);
            item.available= false;
            item.borrowedBy= this;
            item.borrowedDate = borrowDate;
            item.overdueDate = borrowDate.plusDays(OVER_DUE);
            System.out.println(name+" successfully borrowed! " + item.name);
        }
        
    }
    private boolean borrowableCheck(LibraryItems item) {
  
        
        if (borrowedItemsCount>= MAX_ITEMS) {
            System.err.println(name +" cannot borrow " + item.name +", since the borrow limit has been reached!");
            return false;
        } else if ( ! item.available) {
            System.err.println(name +" cannot borrow " + item.name +", it is not available!");
            return false;
        } else if (penalty>=PENALTY_THRESHOLD) {
            System.err.printf(name +" cannot borrow " + item.name +", you must first pay the penalty amount! %s$",PENALTY_THRESHOLD);
            return false;
        }
        return true;
    }

    @Override
    void returnItem(LibraryItems item) {
        borrowedItemsCount-=1;
        borrowedItemsIDs.remove(item.id);
        item.available= true;
        System.out.println(name+ " successfully returned " + item.name);
    }


    
    

}
class Guest extends Users {
    final int  MAX_ITEMS = 3;
    final int  OVER_DUE = 15;
    final String RESTRICTED_ITEM1 = "rare";
    final String RESTRICTED_ITEM2 = "limited";
    String occupation;

    public Guest(String name,String id, String pNumber,String occupation) {
        this.name=name;
        this.id=id;
        this.pNumber=pNumber;
        this.occupation= occupation;
    }

    @Override
    public String toString() {
        return super.toString()+ "\nOccupation: " + occupation;
    }

    @Override
    public void borrow(LibraryItems item,LocalDate borrowDate) {
        boolean isBorrowable = borrowableCheck(item);
        if  (!isBorrowable) {
            return;
        }
        else { 
            borrowedItemsCount+=1;
            borrowedItemsIDs.add(item.id);
            item.available= false;
            item.borrowedBy=this;
            item.borrowedDate= borrowDate;
            item.overdueDate= borrowDate.plusDays(OVER_DUE);
            System.out.println(name+" successfully borrowed! " + item.name);

        }
        
    }
    private boolean borrowableCheck(LibraryItems item) {
  
        
        if (borrowedItemsCount>= MAX_ITEMS) {
            System.err.println(name +" cannot borrow " + item.name +", since the borrow limit has been reached!");
            return false;
        } else if ( ! item.available) {
            System.err.println(name +" cannot borrow " + item.name +", it is not available!");
            return false;
        } else if (penalty>=PENALTY_THRESHOLD) {
            System.err.printf(name +" cannot borrow " + item.name +", you must first pay the penalty amount! %s$\n",PENALTY_THRESHOLD);
            return false;
        } else if (item.type.equals(RESTRICTED_ITEM1) || item.type.equals(RESTRICTED_ITEM2)) {
            System.err.println(name +" cannot borrow " + item.type +" item!");
            return false;
        }
        return true;
    }

    @Override
    void returnItem(LibraryItems item) {
        borrowedItemsCount-=1;
        borrowedItemsIDs.remove(item.id);
        item.available=true;
        System.out.println(name+ " successfully returned " + item.name);
    }


    
}
public class Main {

    public static void main(String[] args) {
        LibraryDBMS myLib= new LibraryDBMS();
        String itemFile = args[0];
        String userFile=  args[1];
        String commandFile= args[2];
        myLib.registerItems(itemFile);
        myLib.registerUsers(userFile);
        myLib.commandExecuter(commandFile);


    }
}
