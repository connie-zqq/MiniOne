//package edu.northeastern.minione.repository;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import edu.northeastern.minione.model.User;
//
//@RunWith(SpringRunner.class)
//@DataJpaTest
//public class UserRepositoryTest {
//
//    @Autowired
//    private UserRepository repository;
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Test
//    public void testFindByUserName() {
//        User user1 = new User("username-1", "firstname-1", "lastname-1", "some-email@test.com");
//        user1.setPasswordHash("some-password-hash");
//        Long id1 = entityManager.persistAndGetId(user1, Long.class);
//        assertThat(id1).isEqualTo(1);
//
//        User actual = repository.findByUserName(user1.getUserName());
//        assertThat(actual).isNotNull();
//        assertThat(actual.getFirstName()).isEqualTo(user1.getFirstName());
//        assertThat(actual.getEmail()).isEqualTo(user1.getEmail());
//
//        User notFound = repository.findByUserName("username-not-found");
//        assertThat(notFound).isNull();
//    }
//}
