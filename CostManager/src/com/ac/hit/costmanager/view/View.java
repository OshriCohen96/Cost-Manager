
package com.ac.hit.costmanager.view;
import com.ac.hit.costmanager.model.*;
import com.ac.hit.costmanager.model.Currency;
import com.ac.hit.costmanager.viewmodel.IViewModel;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

import static com.ac.hit.costmanager.model.DerbyDBModel.ExpensesList;
import static com.ac.hit.costmanager.model.DerbyDBModel.user;


public class View implements IView
{
    private IViewModel vm;
    private ApplicationUI ui;

    @Override
    public void showMessage(String text) { }



    @Override
    public void setViewModel(IViewModel vm) { this.vm = vm; }

    public View() {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                View.this.ui = new ApplicationUI();

                try {
                    View.this.ui.start();
                } catch (CostManagerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public class ApplicationUI
    {

        private DerbyDBModel dataBase;
        private  JDatePanelImpl  datePanel, datePanel2;
        private Expense expense;
        private String sign;
        private JDatePickerImpl datePicker,datePicker2;
        private JLabel titleLabel, balance, descriptionLbl, categoryLbl;
        private JComboBox cbChosenCurrency, categoriesJCombox;
        private JFrame frame;
        private JButton expenseBtn, reportBtn, homeBtn, newCategoryBtn;
        private JPanel panelBtn, panelTop, contentPanel, buttonSection, btnPlaceholder, expensePanel, categorySection;
        private float sum = 0;
        private JTable expenseList;
        private int index, countArray;
        private JFreeChart jFreeChart;
        private ChartFrame chartFrame;
        private UtilDateModel model,model2;
        public List<String> categories = new ArrayList<String>();


        public void start() throws CostManagerException {

            frame = new JFrame("Cost Manager");
            frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent event) {
                    frame.setVisible(false);
                    frame.dispose();
                    System.exit(0);
                }
            });

            //Bar design
            expenseBtn = new JButton("Expenses");
            reportBtn = new JButton("Reports");
            homeBtn = new JButton("Home");
            panelBtn = new JPanel();
            panelTop = new JPanel();
            contentPanel = new JPanel();
            titleLabel = new JLabel();
            Container container = frame.getContentPane();
            panelBtn.setBackground(Color.gray);
            panelTop.setBackground(Color.lightGray);
            contentPanel.setBackground(Color.lightGray);
            panelBtn.setLayout(new BorderLayout());
            panelTop.setLayout(new FlowLayout());
            contentPanel.setLayout(new BorderLayout());
            container.setLayout(new BorderLayout());
            panelTop.add(homeBtn);
            panelTop.add(expenseBtn);
            panelTop.add(reportBtn);
            panelBtn.add("North", titleLabel);
            panelBtn.add(contentPanel);
            container.add("North", panelTop);
            container.add("Center", panelBtn);

            ActionListener homeAction = new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    try {
                        homePage();
                    } catch (CostManagerException e) {
                        e.printStackTrace();
                    }
                }
            };
            homeBtn.addActionListener(homeAction);

            ActionListener expenseAction = new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    try {
                        expensePage();
                    } catch (CostManagerException e) {
                        e.printStackTrace();
                    }
                }
            };
            expenseBtn.addActionListener(expenseAction);


            ActionListener reportAction = new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    reportsPage();
                }
            };
            reportBtn.addActionListener(reportAction);
            frame.setVisible(true);
            homePage();

        }
        /**
         * homepage is the start of the UI
         * the user see is costmanager balance,
         * and also see the shows the distribution of expenses in a jfreechart  graph
         */
        public void homePage() throws CostManagerException {

            //The home page screen UI
            titleLabel.setText("Welcome");
            contentPanel.removeAll();
            frame.setSize(800, 450);
            balance = new JLabel();
            String sign = "Welcome";
            //Show the current data about the user in the home page by the default currency -ILS
            vm.getUserData();
            balance.setText("Your current account expense status: " + String.valueOf(user.getSumExpenses()) + "₪");
            contentPanel.add("North", balance);
            //make a jfree chart that shows the distribution of expenses
            DefaultCategoryDataset data = new DefaultCategoryDataset();
            for(int i = 0; i < user.categoryArrayList.size(); ++i)
                data.setValue(((Category)user.categoryArrayList.get(i)).getCategorySum(), ((Category)user.categoryArrayList.get(i)).getName(), ((Category)user.categoryArrayList.get(i)).getName());
            jFreeChart = ChartFactory.createBarChart3D("Balance Report:", "Categories ", "Total expenses", data, PlotOrientation.VERTICAL, true, true, false);
            CategoryPlot plot = jFreeChart.getCategoryPlot();
            plot.setRangeGridlinePaint(Color.gray);
            chartFrame = new ChartFrame("Your Report:", jFreeChart, true);
            ChartPanel chartPanel = new ChartPanel(jFreeChart);
            chartPanel.setBackground(Color.gray);
            contentPanel.add(chartPanel, "Center");

        }
        /**
         * shows the user his last 10 expenses in a table,and option to go to add new expense (addExpenseBtn)
         */
        public void expensePage() throws CostManagerException
        {
            contentPanel.removeAll();
            categories = new ArrayList<String>();
            categories = vm.getCategories();
            titleLabel.setText("Your last 10 expenses :");
            frame.setSize(800, 450);
            buttonSection = new JPanel();

            /**
             Create an table that show to the user his last 10 expenses
             */

            buttonSection.setBounds(20, 30, 100, 200);
            buttonSection.setLayout(new GridLayout(11, 1, 1, 1));
            String[][] table = new String[11][];
            String[] columnNames = {"ID", "DESCRIPTION", "CATEGORY", "DATE", "PRICE", "CURRENCY"};
            table[0] = columnNames;
            index = 1;
            countArray = ExpensesList.size();
            btnPlaceholder = new JPanel();
            if(countArray>10)
            {
                //take the last 10 expenses from the arraylist expenses
                for (int i = (countArray - 1); i >= (countArray - 10); i--) {
                    String[] row = new String[6];
                    row[0] = ExpensesList.get(i).getExpenseId();
                    row[1] = ExpensesList.get(i).getName();
                    row[2] = ExpensesList.get(i).getCategoryName();
                    row[3] = ExpensesList.get(i).getDate().toString();
                    row[4] = String.valueOf(ExpensesList.get(i).getPrice());
                    row[5] = ExpensesList.get(i).getCurrency().toString();
                    table[index] = row;
                    index += 1;
                }
                expenseList = new JTable(table, columnNames);
                expenseList.setBounds(80, 90, 300, 400);
                contentPanel.add(expenseList);
                expenseList.setBackground(Color.lightGray);
                contentPanel.add("East", buttonSection);
            }
            JButton addExpenseBtn = new JButton("Add New Expense");
            contentPanel.add("South", addExpenseBtn);

            ActionListener addAction = new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    try {
                        addExpense();
                    } catch (CostManagerException e) {
                        e.printStackTrace();
                    }

                }
            };
            addExpenseBtn.addActionListener(addAction);
        }

        /**
         * addExpense method will add new input expense to the table by the user input
         */

        public void addExpense() throws CostManagerException
        {

            contentPanel.removeAll();
            frame.setSize(800, 450);
            vm.getUserData();
            titleLabel.setText("Add new expense");
            expensePanel = new JPanel();
            expensePanel.setLayout(new GridLayout(12, 1, 0, 0));
            descriptionLbl = new JLabel("Description:");
            JTextField descriptionTxt = new JTextField();
            categories = vm.getCategories();
            String[] categoriesNames = new String[categories.size()];
            for (int i = 0; i < categories.size(); i++)
                categoriesNames[i] = categories.get(i);
            categoriesJCombox = new JComboBox(categoriesNames);
            categorySection = new JPanel();
            categorySection.add(categoriesJCombox);
            categoryLbl = new JLabel("Category:");
            categorySection.setLayout(new GridLayout(1, 2, 0, 0));
            newCategoryBtn = new JButton("Add a new category");
            categorySection.add(newCategoryBtn);
            UtilDateModel model = new UtilDateModel();
            JDatePanelImpl datePanel = new JDatePanelImpl(model);
            JDatePickerImpl datePicker = new JDatePickerImpl(datePanel);
            datePicker.setBackground(Color.white);
            JLabel dateLbl = new JLabel("Date :");
            JLabel sumLbl = new JLabel("Price:");
            JTextField sumTxt = new JTextField();
            JLabel currencyLbl = new JLabel("Currency:");
            JTextField currencyTxt = new JTextField();
            JComboBox currenciesJCombox;
            String[] Currencies = {"ILS", "USD", "EUR"};
            currenciesJCombox = new JComboBox(Currencies);
            JPanel currencySection = new JPanel();
            currencySection.setLayout(new GridLayout(1, 2, 0, 0));
            JScrollPane scrollPane2 = new JScrollPane();
            currencySection.add(scrollPane2);
            JButton submitBtn1 = new JButton("Add Expense");

            // adding to the panel all the buttons and elements
            expensePanel.add(descriptionLbl);
            expensePanel.add(descriptionLbl);
            expensePanel.add(descriptionTxt);
            expensePanel.add(categoryLbl);
            expensePanel.add(categorySection);
            expensePanel.add(dateLbl);
            expensePanel.add(datePicker);
            expensePanel.add(sumLbl);
            expensePanel.add(sumTxt);
            expensePanel.add(currencyLbl);
            expensePanel.add(currenciesJCombox);
            expensePanel.add(submitBtn1);

            contentPanel.add("Center", expensePanel);

            ActionListener addCategoryAction = new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    addCategory();
                }
            };
            newCategoryBtn.addActionListener(addCategoryAction);

            ActionListener addAction = new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    try {
                        //Take all the user input's to a Expense value
                        Expense expense;
                        String name = descriptionTxt.getText();
                        double price = Double.parseDouble(sumTxt.getText());
                        Currency currency = Currency.valueOf(currenciesJCombox.getSelectedItem().toString());
                        String category = categoriesJCombox.getSelectedItem().toString();


                        Date selectedDate = (Date) datePicker.getModel().getValue();
                        String id = UUID.randomUUID().toString();
                            expense = new Expense(id, name, price, currency,selectedDate, category);
                            //add the expense to the DB
                             vm.addExpense(expense);
                              expensePage();

                    } catch (CostManagerException e) {
                        e.printStackTrace();
                    }
                }
            };
            submitBtn1.addActionListener(addAction);
        }
        /**
         * addCategory method allows the user to add a new category by his request
         */
        public void addCategory() {
            titleLabel.setText("New Category");
            contentPanel.removeAll();
            frame.setSize(450, 300);
            JTextField nameTxt = new JTextField();
            JPanel formArea = new JPanel();
            formArea.setLayout(new GridLayout(3, 1, 0, 0));
            JLabel nameLbl = new JLabel("Add new name Category :");
            JButton submitBtn = new JButton("Add new Category");
            formArea.add(nameLbl);
            formArea.add(nameTxt);
            formArea.add(submitBtn);
            contentPanel.add("Center", formArea);
            ActionListener addAction = new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    String name = nameTxt.getText();
                    try {
                        //send the new category name
                        vm.addCategory(name);
                    } catch (CostManagerException e) {
                        e.printStackTrace();
                    }
                    try {
                        //add the new category name to the categories list
                        categories.add(name);
                        //Make a sort to the array list
                        Collections.sort(categories);
                        String[] categoriesNames = new String[categories.size()];
                        for (int i = 0; i < categories.size(); i++)
                            categoriesNames[i] = categories.get(i);
                        addExpense();
                    } catch (CostManagerException e) {
                        e.printStackTrace();
                    }
                }
            };
            submitBtn.addActionListener(addAction);
        }
        /**
         * reportsPage method ask the user start date and end date an report that shows all th expenses between the dates.
         */
        public void reportsPage()
        {
//Make the date pickers panel's
            model = new UtilDateModel();
            datePanel = new JDatePanelImpl(model);
            datePicker = new JDatePickerImpl(datePanel);
            model2 = new UtilDateModel();
            datePanel2 = new JDatePanelImpl(model2);
             datePicker2 = new JDatePickerImpl(datePanel2);
            titleLabel.setText("Select specific period:");
            contentPanel.removeAll();
            frame.setSize(650, 350);
            JPanel formArea = new JPanel();
            formArea.setLayout(new GridLayout(5, 1, 0, 0));
            formArea.setBackground(Color.lightGray);
            JPanel buttonsArea = new JPanel();
            buttonsArea.setLayout(new GridLayout(1, 2, 0, 0));
            JLabel firstDateLbl = new JLabel("First Date :");
            JTextField firstDateTxt = new JTextField();
            JLabel secondDateLbl = new JLabel("Second Date :");
            JTextField secondDateTxt = new JTextField();
            JButton reportBtn = new JButton("Make an report");
            buttonsArea.setBackground(Color.white);
            buttonsArea.add(reportBtn);
            formArea.add(firstDateLbl);
            formArea.add(datePicker);
            formArea.add(secondDateLbl);
            formArea.add(datePicker2);
            formArea.add(buttonsArea);
            contentPanel.add("Center", formArea);


            ActionListener addAction = new ActionListener() {
                public void actionPerformed(ActionEvent event) {

                    Date selectedDate = (Date) datePicker.getModel().getValue();
                    Date selectedDate2 = (Date) datePicker2.getModel().getValue();
                    ArrayList<Expense> expensesReport = new ArrayList<>();

                    try {
                        expensesReport = vm.getReport(selectedDate, selectedDate2);
                    } catch (CostManagerException e) {
                        e.printStackTrace();
                    }

                    /**
                     * showPieChart method display the input Expenses to pie chart display by counting
                     * the categories of all the items
                     */

                    DefaultPieDataset pieDataset = new DefaultPieDataset();
                    for(int i=0; i<expensesReport.size(); i++)
                        pieDataset.setValue(expensesReport.get(i).getCategoryName(),expensesReport.get(i).getPrice());
                    JFreeChart chart = ChartFactory.createPieChart("",pieDataset,true,true,true);
                    PiePlot p= (PiePlot)chart.getPlot();
                    ui.frame = new ChartFrame("Pie Chart", chart);
                    ui.frame.setLocation(380,150);
                    ui.frame.setVisible(true);
                    ui.frame.setSize(600,400);

//send the date to specificReport to make an report
                    try {
                        specificReport(selectedDate, selectedDate2);
                    } catch (CostManagerException e) {
                        e.printStackTrace();
                    }


                }
            };

            reportBtn.addActionListener(addAction);
        }
        /**
         * specificReport  method get 2 dates (start date and end date) and make an report that shows all th expenses between the dates.
         */

        public void specificReport(Date date1, Date date2) throws CostManagerException
        {
            contentPanel.removeAll();
            ArrayList<Expense> expensesReport = new ArrayList<>();
            expensesReport = vm.getReport(date1, date2);
           // showPieChart(expensesReport);
            titleLabel.setText("Detailed report");
            frame.setSize(800, 450);
            JLabel balance = new JLabel();
            contentPanel.add("North", balance);
            String sign = "Report you requested between date:" + date1 + "and date" + date2;
            balance.setText(sign);
            //create a table with detailed report about all the expenses between the dates
            buttonSection = new JPanel();
            buttonSection.setBounds(20, 30, 100, 200);
            buttonSection.setLayout(new GridLayout(11, 1, 1, 1));
            String[][] table = new String[expensesReport.size()+1][];
            String[] columnNames = {"ID", "DESCRIPTION", "CATEGORY", "DATE", "PRICE", "CURRENCY"};
            table[0] = columnNames;
            int index = 1;
            int countArray = expensesReport.size();
            btnPlaceholder = new JPanel();
            for (int i = 0; i <expensesReport.size(); i++) {
                String[] row = new String[6];
                row[0] = expensesReport.get(i).getExpenseId();
                row[1] = expensesReport.get(i).getName();
                row[2] = expensesReport.get(i).getCategoryName();
                row[3] = expensesReport.get(i).getDate().toString();
                row[4] = String.valueOf(expensesReport.get(i).getPrice());
                row[5] = expensesReport.get(i).getCurrency().toString();
                table[index] = row;
                index += 1;
            }

            expenseList = new JTable(table, columnNames);
            expenseList.setBounds(80, 90, 250, 300);
            contentPanel.add(expenseList);
            expenseList.setBackground(Color.lightGray);
            contentPanel.add("East", buttonSection);

        }
    }
}
