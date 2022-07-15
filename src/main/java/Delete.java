import entity.Category;
import entity.Characteristic;
import entity.Product;
import entity.Value;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Scanner;

public class Delete {

    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();

        TypedQuery<Product> productsQuery = manager.createQuery(
                "select p from Product p order by p.name", Product.class);

        List<Product> products = productsQuery.getResultList();
        for (Product product : products) {
            System.out.println(product.getName() + " [" + product.getId() + "]");
        }

        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Выберите товар");
            long prodInput = Long.parseLong(scanner.nextLine());
            Product product = manager.find(Product.class, prodInput);
            manager.getTransaction().begin();
            manager.remove(product);


                manager.getTransaction().commit();

         }catch (Exception e){
                manager.getTransaction().rollback();
                e.printStackTrace();
            }

        }
    }
