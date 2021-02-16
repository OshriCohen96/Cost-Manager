

package com.ac.hit.costmanager.model;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * DerbyDBModel class store and remove data from the DB by the View request through the ViewModel
 * it's implements the IModel interface
 */
public class DerbyDBModel implements IModel
{
        static public User user;
        //ExpensesList is a static list that keep all the expenses
        static public ArrayList<Expense> ExpensesList=new ArrayList<>();
        //categories is a static list that keep all the categories names
        static public List<String> categories=new ArrayList<>();
        //categoryArrayList is a static list that keep all the expenses and categories
        static public ArrayList<Category> categoryArrayList=new ArrayList<>();
        final String databaseConnectionString = "jdbc:derby:";
        private String dbName = "derbyDB";


        /**
         *  addExpense method will add new input expense to the table
         */
        public void addExpense(Expense expense) throws CostManagerException
        {
                //Adding new expense to the table
                try (Connection conn = getConnection()) {
                        String query = "insert into TES11 (ExpenseID, nameExpense ,Price, Category,Currency,Date)\n" +
                                "values (" +
                                "'" + expense.getExpenseId() + "'," +
                                "'" + expense.getName() + "'," +
                                expense.getPrice() + "," +
                                "'" + expense.getCategoryName() + "'," +
                                "'" + expense.getCurrency().name() + "'," +
                                expense.getDate().getTime() + ")";
                        System.out.println(query);
                        conn.createStatement().execute(query);
                        categories=getCategories();
                        if (!categories.contains(expense.getCategoryName()))
                        {
                                addCategory(expense.getCategoryName());
                        }

                } catch (SQLException e) {
                        System.out.println(e);
                        throw new CostManagerException("DB access failed", e);
                }
        }

        /**
         *   getExpensesFromDB method takes the data from the table and updates it in the static lists
         */
        public ArrayList<Expense> getExpensesFromDB() throws CostManagerException
        {   //Adding all the lines int the table to arrayList
                ExpensesList=new ArrayList<>();
                Expense expense = null;
                try (Connection conn = getConnection();)
                {
                        ResultSet resultSet = conn.createStatement().executeQuery("select * from TES11");

                        double price;
                        String currency1 = null,id=null,name = null,category=null;
                        while (resultSet.next()) {

                                id=resultSet.getString("ExpenseID");
                                name=resultSet.getString("nameExpense");
                                price=resultSet.getDouble("Price");
                                category=resultSet.getString("Category");
                                currency1=resultSet.getString("Currency");
                                long date=resultSet.getLong( "Date");
                                Currency currency = Currency.valueOf(currency1);
                                Date date1=new Date(date);
                                ExpensesList.add(new Expense(id,name,price, currency, date1, category));
                        }

                } catch (SQLException | CostManagerException e )
                {
                        System.out.println(e);
                        throw new CostManagerException("DB access failed", e);
                }
                return (ExpensesList);

        }

        /**
         *   deleteExpense method remove item from the table by her ID
         */

        public void deleteExpense(String ExpenseID) throws CostManagerException
        {
                //Delete an expense from the table by her ID

                if (validInput(ExpenseID))

                        try (Connection conn = getConnection()) {
                                conn.createStatement().execute("delete from TES11 where ExpenseID='" + ExpenseID + "' \n");//

                        } catch (SQLException e) {
                                System.out.println(e);
                                throw new CostManagerException("DB access failed", e);
                        }
        }
        /**
         *   getCategories method update all the categories to a list
         */

        public List<String> getCategories() throws CostManagerException
        {
                //get all the existing CATEGORIES in the DB to a list
                try (Connection conn = getConnection();
                ) {
                        ResultSet resultSet = conn.createStatement().executeQuery("select Category from CATEGORIES");
                        categories = new ArrayList<String>();

                        //categories that should be  defined By default
                        categories.add("Food");
                        categories.add("Car expenses");
                        categories.add("Beauty");
                        categories.add("Education");
                        categories.add("clothing");


                        while (resultSet.next()) {
                                categories.add(resultSet.getString("Category"));
                        }

                } catch (SQLException e) {
                        System.out.println(e);
                        throw new CostManagerException("DB access failed", e);
                }
                //Make a sort to the array list
                Collections.sort(categories);
                return categories;
        }

        /**
         *   addCategory method add a new category to the table by the user request
         */
        public void addCategory(String categoryName) throws CostManagerException {

                getUserData();
                // Add new category to the CATEGORIES table
                if (validInput(categoryName))
                        try (Connection conn = getConnection()) {

                                String query = "insert into CATEGORIES (Category)\n" +
                                        "values ('" +
                                        categoryName + "')";
                                System.out.println(query);
                                conn.createStatement().execute(query);

                        } catch (SQLException e) {
                                System.out.println(e);
                                throw new CostManagerException("DB access failed", e);
                        }


        }

        /**
         *   getReport method get 2 dates (start date and end date) and make an report that shows all th expenses between the dates.
         */

        @Override
        public ArrayList<Expense> getReport(Date date1, Date date2) throws CostManagerException
        {
                //Gets two dates and take from the DB all the expenses that made during this time period
                validateDates(date1, date2);
                ArrayList<Expense> expensesReport = new ArrayList<>();
                String currency1;
                long date;
                try (Connection conn = getConnection()) {

                        ResultSet resultSet = conn.createStatement().executeQuery("SELECT * from TES11 where Date>=" + date1.getTime() + " AND Date<=" + date2.getTime() + " \n");//todo
                        while (resultSet.next()) {
                                currency1 = resultSet.getString("Currency");
                                Currency currency = Currency.valueOf(currency1);
                                date = resultSet.getLong("Date");
                                Date castDate = new Date(date);
                                Expense expense1;
                                expense1 = new Expense(resultSet.getString("ExpenseID"),resultSet.getString("nameExpense"), resultSet.getDouble("Price"), currency, castDate,resultSet.getString("Category"));
                                expensesReport.add(expense1);
                        }
                        System.out.println(resultSet);
                } catch (SQLException e) {
                        System.out.println(e);
                        throw new CostManagerException("DB access failed", e);
                }

                return (expensesReport);
        }
        /**
         *   getUserData method get all the data about the user .
         *   all the categories -and the expenses that contains them
         */


        @Override
        public void getUserData() throws CostManagerException
        {
                getExpensesFromDB();
                categories= getCategories();
                categoryArrayList=new ArrayList<>();
                for(int i=0;i<categories.size();i++){
                        Category category=new Category(categories.get(i),0);
                        categoryArrayList.add(category);
                }
                for (int i = 0; i < categoryArrayList.size(); i++) {

                        for (int j = 0; j < ExpensesList.size(); j++)
                        {

                                if (categoryArrayList.get(i).getName().equals(ExpensesList.get(j).getCategoryName()))
                                {
                                        categoryArrayList.get(i).addNewExpense(ExpensesList.get(j));

                                }

                        }
                }
                user=new User(categoryArrayList,0);

        }

        /**
         *   validateDates method Checks the validity of two dates
         */
        private static void validateDates(Date date1, Date date2) throws CostManagerException
        {
                if (date1.getTime() > date2.getTime())
                        throw new CostManagerException("please enter valid dates");
        }



        /**
         *   validInput method check the input isn't NULL or empty
         */

        private static boolean validInput(String param)
        {
                if (param == null || param.trim().length() == 0)
                        return false;
                return true;
        }

        public DerbyDBModel() throws CostManagerException {
                createTables(false);
        }

        public DerbyDBModel(String dbName) throws CostManagerException {

                this.dbName = dbName; // derbyDB
                createTables(true);
        }

        private void createTables(boolean forceRecreate) throws  CostManagerException {

                try (Connection conn = getConnection(true)) {
                        DatabaseMetaData metadata = conn.getMetaData();

                        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
                        Class.forName(driver).newInstance();

                        // drop tables if need
                        if (forceRecreate && metadata.getTables(null, null, "TES11", null).next()) {
                                conn.createStatement().execute("DROP TABLE TES11");
                        }


                        if (forceRecreate && metadata.getTables(null, null, "CATEGORIES", null).next()) {
                                conn.createStatement().execute("DROP TABLE CATEGORIES");
                        }

                        // create tables
                        if (!metadata.getTables(null, null, "TES11", null).next()) {
                                conn.createStatement().execute("CREATE TABLE TES11 (\n" +
                                        "    ExpenseID varchar(36),\n" +
                                        "    nameExpense varchar(255),\n" +
                                        "    Price REAL,\n" +
                                        "    Category varchar(255),\n" +
                                        "    Currency varchar(255),\n" +
                                        "    Date BIGINT,\n" +
                                        "    PRIMARY KEY (ExpenseID)\n" +
                                        ")");
                        }



                        if (!metadata.getTables(null, null, "CATEGORIES", null).next()) {
                                conn.createStatement().execute("CREATE TABLE CATEGORIES (\n" +
                                        "    Category varchar(255),\n" +
                                        "unique (Category))");
                        }

                } catch (SQLException | ClassNotFoundException e) {
                        System.out.println(e);
                        throw new CostManagerException("DB access failed", e);
                } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new CostManagerException("DB access failed", e);
                } catch (InstantiationException e) {
                        e.printStackTrace();
                        throw new CostManagerException("DB access failed", e);
                }
        }

        private Connection getConnection() throws CostManagerException {
                return getConnection(false);
        }

        private Connection getConnection(boolean toCreate) throws CostManagerException
        {
                try {
                        String url = databaseConnectionString + dbName;
                        if (toCreate) {
                                url = url + ";create=true";
                        }
                        return DriverManager.getConnection(url, null);
                }catch  (SQLException e){
                        System.out.println(e);
                        throw new CostManagerException("DB access failed", e);
                }
        }
}

