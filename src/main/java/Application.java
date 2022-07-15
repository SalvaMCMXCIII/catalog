import entity.Category;
import entity.Characteristic;
import entity.Product;
import entity.Value;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.sound.midi.Soundbank;
import java.util.List;
import java.util.Scanner;

public class Application {

    private static final EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory("main");
    private static final Scanner INPUT = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Меню");
        System.out.println("[1] - Create");
        System.out.println("[2] - Update");
        System.out.println("[3] - Delete");

        int select = Integer.parseInt(INPUT.nextLine());
        switch (select) {
            case 1:
                create();
                break;
            case 2:
                update();
                break;
            case 3:
                delete();
                break;
            default:
                System.out.println("Введите корректное значение");
                break;
        }

    }

    private static void create() {
        EntityManager manager = FACTORY.createEntityManager();

        try {
            TypedQuery<Category> categoriesQuery = manager.createQuery(
                    "select c from Category c order by c.name", Category.class);

            List<Category> categories = categoriesQuery.getResultList();
            for (Category category : categories) {
                System.out.println(category.getName() + " [" + category.getId() + "]");
            }

            System.out.println("Выберите категорию:");
            long input = Long.parseLong(INPUT.nextLine());
            TypedQuery<Long> categoryCountQuery = manager.createQuery(
                    "select count (c.id) from Category c where c.id = ?1 ", Long.class);
            categoryCountQuery.setParameter(1, input);
            long categoryCount = categoryCountQuery.getSingleResult();
            while (categoryCount == 0) {
                System.out.println("Данной категории нет");
                System.out.println("Выберите категорию:");

                input = Long.parseLong(INPUT.nextLine());
                categoryCountQuery = manager.createQuery(
                        "select count (c.id) from Category c where c.id = ?1 ", Long.class);
                categoryCountQuery.setParameter(1, input);
                categoryCount = categoryCountQuery.getSingleResult();
            }
            System.out.println("Введите название");
            String txtInput = INPUT.nextLine();
            System.out.println("Введите описание товара");
            String descInput = INPUT.nextLine();
            System.out.println("Введите цену товара");
            String priceInput = INPUT.nextLine();
            while (!priceInput.matches("\\d+")) {
                System.out.println("Введите корректное значение");
                priceInput = INPUT.nextLine();
            }
            manager.getTransaction().begin();

            Product product = new Product();
            Category category = manager.find(Category.class, input);
            product.setCategory(category);
            product.setName(txtInput);
            product.setDescription(descInput);
            product.setPrice(Integer.parseInt(priceInput));
            manager.persist(product);


            List<Characteristic> characteristics = category.getCharacteristics();

            for (Characteristic characteristic : characteristics) {
                System.out.println(characteristic.getCharacteristic() + " [" + characteristic.getId() + "]");
                String characInput = INPUT.nextLine();
                Value value = new Value();
                value.setProduct(product);
                value.setCharacteristic(characteristic);
                value.setValue(characInput);
                manager.persist(value);
            }


            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            manager.close();
        }

    }

    private static void update() {

        EntityManager manager = FACTORY.createEntityManager();

        try {
            TypedQuery<Product> productsQuery = manager.createQuery(
                    "select p from Product p order by p.name", Product.class);

            List<Product> products = productsQuery.getResultList();
            for (Product product : products) {
                System.out.println(product.getName() + " [" + product.getId() + "]");
            }

            System.out.println("Выберите товар");
            long prodInput = Long.parseLong(INPUT.nextLine());
            Product product = manager.find(Product.class, prodInput);
            System.out.println("Название товара:" + " " + product.getName());
            String setInput = INPUT.nextLine();
            System.out.println("Описание товара:" + " " + product.getDescription());
            String descInput = INPUT.nextLine();
            System.out.println("Ценна:" + " " + product.getPrice());
            String priceInput = INPUT.nextLine();
            manager.getTransaction().begin();

            if (!setInput.equals("")) {
                product.setName(setInput);
            }
            if (!descInput.equals("")) {
                product.setDescription(descInput);
            }
            if (!priceInput.equals("")) {
                product.setPrice(Integer.parseInt(priceInput));

            }
            List<Characteristic> characteristicList = product.getCategory().getCharacteristics();
            for (Characteristic characteristic : characteristicList) {

                TypedQuery<Value> valueQuery = manager.createQuery(
                        "select v from Value v where v.product.id = ?1 and v.characteristic.id = ?2 ", Value.class);
                valueQuery.setParameter(1, product.getId());
                valueQuery.setParameter(2, characteristic.getId());
                List<Value> values = valueQuery.getResultList();

                if (values.size() == 0) {
                    System.out.println(characteristic.getCharacteristic() + ": ");
                    String valueInput = INPUT.nextLine();
                    Value value = new Value();
                    value.setValue(valueInput);
                    value.setProduct(product);
                    value.setCharacteristic(characteristic);
                    manager.persist(value);

                } else {
                    System.out.println(characteristic.getCharacteristic() + ": " + values.get(0).getValue());
                    String inputVal2 = INPUT.nextLine();
                    if (!inputVal2.equals("")) {
                        values.get(0).setValue(inputVal2);
                    }

                }

            }

            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            manager.close();
        }

    }

    private static void delete(){
        EntityManager manager = FACTORY.createEntityManager();

        try {
            TypedQuery<Product> productsQuery = manager.createQuery(
                    "select p from Product p order by p.name", Product.class);

            List<Product> products = productsQuery.getResultList();
            for (Product product : products) {
                System.out.println(product.getName() + " [" + product.getId() + "]");
            }

            System.out.println("Выберите товар");
            long prodInput = Long.parseLong(INPUT.nextLine());
            Product product = manager.find(Product.class, prodInput);
            manager.getTransaction().begin();
            manager.remove(product);


            manager.getTransaction().commit();

        }catch (Exception e){
            manager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            manager.close();
        }
    }

}







