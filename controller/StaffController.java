/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;


import model.Product;
import com.mysql.cj.protocol.Resultset;
import database.DB_connection;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.time.LocalDate;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import pos.POS;

/**
 * FXML Controller class
 *
 * @author MITUSER-2
 */
public class StaffController implements Initializable {

    @FXML
    private TableView<Product> tb_products;
    @FXML
    private TableColumn<?, ?> colProdctid;
    @FXML
    private TableColumn<?, ?> colCategory;
    @FXML
    private TableColumn<?, ?> colBrand;
    @FXML
    private TableColumn<?, ?> colName;
    @FXML
    private TableColumn<?, ?> colSize;
    @FXML
    private TableColumn<?, ?> colPrice;
    @FXML
    private TableColumn<?, ?> colStock;
    @FXML
    private TableColumn<?, ?> image;
    @FXML
    private TextField txtsearch;
    @FXML
    private Label lblname;
    @FXML
    private Label lbldate;
    @FXML
    private Label lblslip;
    @FXML
    private TableView<Product> tbsales;
    @FXML
    private TableColumn<?, ?> colsProductid;
    @FXML
    private TableColumn<?, ?> colsBrand;
    @FXML
    private TableColumn<?, ?> colsName;
    @FXML
    private TableColumn<?, ?> colsSize;
    @FXML
    private TableColumn<?, ?> colsPrice;
    @FXML
    private TableColumn<Product, Integer> colsQty;
    @FXML
    private TableColumn<?, ?> colsAmount;
    @FXML
    private RadioButton rdCash;
    @FXML
    private ToggleGroup rdPayment;
    @FXML
    private TextField txtTotal;
    @FXML
    private TextField txtReceive;
    @FXML
    private RadioButton rdPay;
    @FXML
    private Button btnCart;
    @FXML
    private TextField txtChange;
    @FXML
    private Button btnPrint;
    @FXML
    private Button btnrefresh;
    private Button btnlogout;
    private Button btnpayment;
    @FXML
    private Button search;
    Connection con = null;
    int total;
    
    ObservableList<Product> productList,cartList;
    
    Statement st;
    PreparedStatement pst;
    ResultSet rs;
    public static String totals;
    public static String receive;
    public static String change;
    String payment = "";
    
    public static int sid;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cartList = FXCollections.observableArrayList();
        DB_connection db = new DB_connection();
        try{
            con = db.getConnection();
            lblname.setText(LoginFxmlController.username);
            LocalDate date = LocalDate.now();
            lbldate.setText(date.toString());
            countSlip();
            initProductList();
            
//            product list
            colProdctid.setCellValueFactory(new PropertyValueFactory("pid"));
            colCategory.setCellValueFactory(new PropertyValueFactory("category"));
            colBrand.setCellValueFactory(new PropertyValueFactory("brand"));
            colSize.setCellValueFactory(new PropertyValueFactory("size"));
            colName.setCellValueFactory(new PropertyValueFactory("name"));
            colPrice.setCellValueFactory(new PropertyValueFactory("price"));
            colStock.setCellValueFactory(new PropertyValueFactory("stock"));
            image.setCellValueFactory(new PropertyValueFactory("imgView"));
            
            tb_products.setItems(productList);
            
//            insert value
            colsProductid.setCellValueFactory(new PropertyValueFactory("pid"));
            colsBrand.setCellValueFactory(new PropertyValueFactory("brand"));
            colsName.setCellValueFactory(new PropertyValueFactory("name"));
            colCategory.setCellValueFactory(new PropertyValueFactory("category"));
            colsSize.setCellValueFactory(new PropertyValueFactory("size"));
            colsPrice.setCellValueFactory(new PropertyValueFactory("price"));
            colsQty.setCellValueFactory(new PropertyValueFactory("qty"));
            colsAmount.setCellValueFactory(new PropertyValueFactory("amount"));
            
//            insert action
            colsQty.setCellFactory(column -> new TableCell<Product,Integer>(){
                private final Button btnInc = new Button("+");
                private final Button btnDec = new Button("-");
                private final Label lblQty = new Label();
                 private final Label lbl1 = new Label("");
                 private final Label lbl2 = new Label("");
                 private final Label lblAmount = new Label("");
                 private final HBox hbox = new HBox(5,btnDec,lbl1,lblQty,lbl2,btnInc,lblAmount);
                 
                 {
                     hbox.setAlignment(Pos.CENTER);
                     btnInc.setStyle("-fx-background-color: tomato;"
                             + "-fx-text-fill: white;");
                     btnDec.setStyle("-fx-background-color: tomato;"
                             + "-fx-text-fill: white;");
                     
                     btnInc.setOnAction(e->{
                        Product product = getTableView().getItems().get(getIndex());
                        product.setQty(product.getQty() + 1);
                        product.setAmount(product.getPrice() * product.getQty());
                        lblQty.setText(String.valueOf(product.getQty()));
                        int index = cartList.indexOf(product);
                        cartList.set(index,product);
                        int total = 0;
                        for(int i=0;i<cartList.size();i++){
                            Product p = cartList.get(i);
//                            System.out.printLn(p.getAmount());
                              total += p.getAmount();
                        }
                        txtTotal.setText(String.valueOf(total));
                        
                     });
                     
                     
                     btnDec.setOnAction(e->{
                        Product product = getTableView().getItems().get(getIndex());
                        if(product.getQty() > 1){
                         product.setQty(product.getQty() - 1);
                         product.setAmount(product.getPrice() * product.getQty());
                        lblQty.setText(String.valueOf(product.getQty())); 
                        int index = cartList.indexOf(product);
                        cartList.set(index,product);
                        int total = 0;
                        for(int i=0;i<cartList.size();i++){
                            Product p = cartList.get(i);
//                            System.out.printLn(p.getAmount());
                              total += p.getAmount();
                        }
                        txtTotal.setText(String.valueOf(total));
                        }
                        
                     });
                 }
                 
                @Override
                protected void updateItem(Integer qty, boolean empty){
                    super.updateItem(qty, empty);
                    if(empty || qty == null){
                        setGraphic(null);
                    }else{
                        Product product = getTableView().getItems().get(getIndex());
                        lblQty.setText(String.valueOf(product.getQty()));
                        setGraphic(hbox);
                    }
                }
                 
            });
            
                       
             /*  remove button */
            TableColumn<Product, Void> colSaction = new TableColumn<>("Action");

            Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = new Callback<>() {
                @Override
                public TableCell<Product, Void> call(final TableColumn<Product, Void> param) {
                    final TableCell<Product, Void> cell = new TableCell<>() {

                        private final Button btn = new Button("Remove");
                        
                        {
                             btn.setStyle("-fx-background-color: tomato; -fx-text-fill: white;");
                            btn.setOnAction((event) -> {
                                Product product = getTableView().getItems().get(getIndex());
                                getTableView().getItems().remove(product); // Remove from UI
                                cartList.remove(product);
                                    int total = 0;
                                 for (int i=0; i<cartList.size();i++){
                                Product p = cartList.get(i);
                                System.out.println(p.getAmount());
                                total+= p.getAmount();
                                 }
                                 txtTotal.setText(String.valueOf(total));
                            });
                        }

                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                setGraphic(btn);
                            }
                        }
                    };
                    return cell;
                }
            };

            colSaction.setCellFactory(cellFactory);
            tbsales.getColumns().add(colSaction);
                
          
            
//            initProductList();
        }catch(ClassNotFoundException ex){
            System.out.println("connection error");
        }catch(SQLException ex){
            
        }
    }    
    

    @FXML
    private void handleCartAction(ActionEvent event) throws SQLException {
        Product p = (Product)tb_products.getSelectionModel().getSelectedItem();
//       JOptionPane.showMessageDialog(null, p.getCategory());

//          
            if(p == null){
                JOptionPane.showMessageDialog(null, "Please select item from table");
            }else{
                String input = JOptionPane.showInputDialog("Enter quanity");
//                String sql = "Insert into carts(product_id,qty) values(?,?)";
//                pst = con.prepareStatement(sql);
//                pst.setInt(1,p.getPid());
//                pst.setInt(2, Integer.parseInt(input));
//                pst.executeUpdate();
                int amount = p.getPrice()*Integer.parseInt(input);
                total+=amount;
               cartList.add(new Product(p.getPid(),p.getCategory(),Integer.parseInt(input),amount,
                       p.getBrand(),p.getPrice(),
                       p.getName(),p.getSize()));
                    tbsales.setItems(cartList);
                    if(txtTotal.getText().isEmpty()){
                        txtTotal.setText(String.valueOf(amount));
                    }else{
                        amount += Integer.parseInt(txtTotal.getText());
                        txtTotal.setText(String.valueOf(amount));
                    }
            }
         
    }
    
    @FXML
    void handlePaymentAction(ActionEvent event) {
        
        if(!rdCash.isSelected() && !rdPay.isSelected()){
            JOptionPane.showMessageDialog(null, "Please select payment type !!!");
        }else{
            btnPrint.setDisable(false);
            if(rdPay.isSelected()){
                payment = "pay";
                txtChange.setText("0");
                txtReceive.setText(String.valueOf(total));
            }else{
                payment = "cash";
                if(txtReceive.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please enter receive amount");
                }else{
                    
                    int change = Integer.parseInt(txtReceive.getText())-Integer.parseInt(txtTotal.getText());
                    txtChange.setText(String.valueOf(change));
                }
            }
            
            if(cartList.size()>0){
                btnPrint.setDisable(false);
            }
        }
    }
    
    @FXML
    void handleReceiveAction(MouseEvent event) {
        
    }

    @FXML
    private void handlePrintAction(ActionEvent event) throws SQLException, IOException {
        int cashier_id = LoginFxmlController.cashier_id;
        totals = txtTotal.getText();
        receive = txtReceive.getText();
        change = txtChange.getText();
        String sql = "Select max(sale_id) As id from sales"; 
        st = con.createStatement(); 
        rs =st.executeQuery(sql);  
        if(rs.next()){
            sid = rs.getInt("id");
            sid++;
            
            String sql1 = "insert into sales values(?,?,?,?,?)"; // 
            pst = con.prepareStatement(sql1); // 
            pst.setInt(1, sid); 
            pst.setInt(2, cashier_id);
            pst.setString(3, payment);
            pst.setInt(4,Integer.parseInt(txtTotal.getText()));
            pst.setString(5, lbldate.getText());  
            pst.executeUpdate(); 
            
           
            
            for(int i=0;i < cartList.size();i++){
                Product p = cartList.get(i);
                
                String sql2 = "insert into sales_details(sales_id,product_id,qty) values(?,?,?)";
                pst = con.prepareStatement(sql2);
                pst.setInt(1, sid);
                pst.setInt(2, p.getPid());
                pst.setInt(3, p.getQty());
                pst.executeUpdate();
            }
//            cartList.removeAll(cartList);
//            txtTotal.setText("");
//            txtReceive.setText("");
//            txtChange.setText("");
//            rdPay.setSelected(false);
//            rdCash.setSelected(false);
            
            Stage stage = POS.stage;
            Parent root = FXMLLoader.load(getClass().getResource("/view/Voucher.fxml"));
        
               Scene scene = new Scene(root);
               stage.setScene(scene);
               stage.show();
            
        }
        
    }
    
    @FXML
    private void handleTableAction(MouseEvent event){
       

        
    }
    
    @FXML
    private void handleLogoutAction(ActionEvent event) throws SQLException, IOException {
        
        Stage stage=POS.stage;
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginFxml.fxml"));
        Scene scene = new Scene(root);
               
        stage.setScene(scene);
                stage.show();
    }
    
    @FXML
    private void handleRefreshAction(ActionEvent event) throws SQLException{
        initProductList();
        tb_products.setItems(productList);
    }

    @FXML
    private void searchbtn(ActionEvent event) throws SQLException {
        if(txtsearch.getText().isEmpty())
            JOptionPane.showMessageDialog(null, "Please enter seach product name !");
        else{
            String sql = "Select * from products where brand like ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, txtsearch.getText()+"%");
            rs = pst.executeQuery();
            boolean found = false;
            productList.removeAll(productList);
            while(rs.next()){
                found = true;
                productList.add(new Product(rs.getInt("product_id"),
                rs.getString("category"),rs.getString("name"),rs.getString("brand"),
                rs.getString("size"),rs.getInt("price"),rs.getInt("stock_qty"),
                rs.getString("img_name")));
            }
            tb_products.setItems(productList);
            
            if(!found){
                initProductList();
                tb_products.setItems(productList);
                JOptionPane.showMessageDialog(null, "Search name is not found !");
            }
        }
    }
    
    public void initProductList() throws SQLException{
        productList = FXCollections.observableArrayList();
        String sql = "Select * from products";
         st = con.createStatement();
         rs = st.executeQuery(sql);
         System.out.println(rs);
        
        while(rs.next()){
            
        productList.add(new Product(rs.getInt("product_id"),
                rs.getString("category"),rs.getString("brand"),rs.getString("name"),
                rs.getString("size"),rs.getInt("price"),rs.getInt("stock_qty"),
                rs.getString("img_name")));
        
        }
    }
    
    public void countSlip() throws SQLException{
        String sql = "Select count(*) AS count from sales";
        st = con.createStatement();
        rs = st.executeQuery(sql);
        if(rs.next()){
            int slip_no =rs.getInt("count")+1;
            lblslip.setText("00"+slip_no);
        }
    }
    
}

