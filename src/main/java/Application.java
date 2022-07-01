import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Application {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();

        TypedQuery<Category> categoriesQuery = manager.createQuery(
                "select c from Category c order by c.name", Category.class);

        List<Category> categories = categoriesQuery.getResultList();
        for (Category category : categories) {
            System.out.println(category.getName() + " [" + category.getId() + "]");
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Выберите категорию:");
        long input = Long.parseLong(scanner.nextLine());
        TypedQuery<Long> categoryCountQuery = manager.createQuery(
                "select count (c.id) from Category c where c.id = ?1 ", Long.class);
        categoryCountQuery.setParameter(1, input);
        long categoryCount = categoryCountQuery.getSingleResult();
        while (categoryCount == 0) {
            System.out.println("Данной категории нет");
            System.out.println("Выберите категорию:");

            input = Long.parseLong(scanner.nextLine());
            categoryCountQuery = manager.createQuery(
                    "select count (c.id) from Category c where c.id = ?1 ", Long.class);
            categoryCountQuery.setParameter(1, input);
            categoryCount = categoryCountQuery.getSingleResult();
        }
        System.out.println("Введите название");
        String txtInput = scanner.nextLine();
        System.out.println("Введите описание товара");
        String descInput = scanner.nextLine();
        System.out.println("Введите цену товара");
        String priceInput = scanner.nextLine();
        while (!priceInput.matches("\\d+")) {
            System.out.println("Введите корректное значение");
            priceInput = scanner.nextLine();
        }



        try {
            manager.getTransaction().begin();

            Product product = new Product();
            Category category = manager.find(Category.class, input);
            product.setCategory(category);
            product.setName(txtInput);
            product.setDescription(descInput);
            product.setPrice(Integer.parseInt(priceInput));
            manager.persist(product);


            List<Characteristic> characteristics = category.getCharacteristics();

            for(Characteristic characteristic : characteristics){
                System.out.println(characteristic.getCharacteristic() + " [" + characteristic.getId() + "]");
                String characInput = scanner.nextLine();
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
        }

    }

}


