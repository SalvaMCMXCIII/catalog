import com.sun.security.jgss.GSSUtil;

import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

public class Update {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();




        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Выберите товар");
            long prodInput = Long.parseLong(scanner.nextLine());
            Product product = manager.find(Product.class, prodInput);
            System.out.println("Название товара:" + " " + product.getName());
            String setInput = scanner.nextLine();
            System.out.println("Описание товара:" + " " + product.getDescription());
            String descInput = scanner.nextLine();
            System.out.println("Ценна:" + " " + product.getPrice());
            String priceInput = scanner.nextLine();
            manager.getTransaction().begin();

            if(!setInput.equals("")){
            product.setName(setInput);
            }
            if(!descInput.equals("")){
            product.setDescription(descInput);
            }
            if(!priceInput.equals("")){
                product.setPrice(Integer.parseInt(priceInput));

            }
            List<Characteristic> characteristicList = product.getCategory().getCharacteristics();
            for(Characteristic characteristic : characteristicList){

                TypedQuery<Value> valueQuery = manager.createQuery(
                        "select v from Value v where v.product.id = ?1 and v.characteristic.id = ?2 ", Value.class);
                valueQuery.setParameter(1,product.getId());
                valueQuery.setParameter(2,characteristic.getId());
                List<Value> values = valueQuery.getResultList();


                if(values.size() == 0){
                    System.out.println(characteristic.getCharacteristic() + ": ");
                    String valueInput = scanner.nextLine();
                    Value value = new Value();
                    value.setValue(valueInput);
                    value.setProduct(product);
                    value.setCharacteristic(characteristic);
                    manager.persist(value);

                }else{
                    System.out.println(characteristic.getCharacteristic() + ": " + values.get(0).getValue());
                    String inputVal2 = scanner.nextLine();
                    if(!inputVal2.equals("")){
                        values.get(0).setValue(inputVal2);

                    }

                }


            }

            manager.getTransaction().commit();
        } catch (Exception e){
            manager.getTransaction().rollback();
            e.printStackTrace();
        }


    }
}



         /*  List<Value> valueList = product.getValues();
            for(Value value : valueList){
                String valueInput = scanner.nextLine();
                if(!valueInput.equals("")){
                    value.setValue(valueInput);

                }


            }*/
