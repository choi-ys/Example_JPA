package domain;

import generator.MemberGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author : choi-ys
 * @date : 2021/03/15 4:42 오후
 * @Content : MmeberEntity를 이용한 JPA CRUD 실행 예제
 */
@DisplayName("JPA CRUD - Domain:MemberEntity")
class MemberEntityTest {

    @Test
    @DisplayName("JAP Create - 회원 생성")
    void createMemberEntity(){
        // Given : Test수행에 필요한 객체 및 데이터 설정
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("example-jpa");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        MemberEntity memberEntity = MemberGenerator.buildMemberEntity();

        entityTransaction.begin();
        try {
            // When : JPA를 이용한 MemberEntity객체의 Insert 쿼리 생성 및 실행
            entityManager.persist(memberEntity);
            entityTransaction.commit();

            // Then : Mapping된 Table에 MemberEntity객체 저장 여부 확인
            MemberEntity savedMemberEntity = entityManager.find(MemberEntity.class, memberEntity.getMemberNo());
            assertThat(savedMemberEntity.getMemberName()).isEqualTo(memberEntity.getMemberName());
        } catch (Exception e) {
            entityTransaction.rollback();
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
    }

    @Test
    @DisplayName("JAP Update - 회원 수정")
    public void updateMemberEntity(){
        // Given : Test수행에 필요한 객체 및 데이터 설정
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("example-jpa");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        MemberEntity memberEntity = MemberGenerator.buildMemberEntity();

        entityTransaction.begin();
        try {
            // When #1 : JPA를 이용한 MemberEntity객체의 Insert 쿼리 생성 및 실행
            entityManager.persist(memberEntity);

            // When #2 : MemberEntity객체의 값 변경 시 JPA를 이용한 Update 쿼리 생성 및 실행
            String updateMemberName = "석용최";
            memberEntity.setMemberName(updateMemberName);
            entityTransaction.commit();

            // Then : Mapping된 Table에 MemberEntity객체의 값 변경으로 인한 Column 갱신 여부 확인
            MemberEntity savedMemberEntity = entityManager.find(MemberEntity.class, memberEntity.getMemberNo());
            assertThat(savedMemberEntity.getMemberName()).isEqualTo(updateMemberName);
        } catch (Exception e) {
            entityTransaction.rollback();
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
    }

    @Test
    @DisplayName("JAP Delete - 회원 삭제")
    public void deleteMemberEntity(){
        // Given : Test수행에 필요한 객체 및 데이터 설정
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("example-jpa");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        MemberEntity memberEntity = MemberGenerator.buildMemberEntity();

        entityTransaction.begin();
        try {
            // When #1 : JPA를 이용한 MemberEntity객체의 Insert 쿼리 생성 및 실행
            entityManager.persist(memberEntity);

            // When #1 : JPA를 이용한 MemberEntity객체의 Delete 쿼리 생성 및 실행
            entityManager.remove(memberEntity);
            entityTransaction.commit();

            // Then : Mapping된 Table에 MemberEntity객체 삭제 여부 확인
            MemberEntity savedMemberEntity = entityManager.find(MemberEntity.class, memberEntity.getMemberNo());
            assertThat(savedMemberEntity).isNull();
        } catch (Exception e) {
            entityTransaction.rollback();
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
    }

    @Test
    @DisplayName("JAP Read - 회원 목록 조회")
    public void selectMemberEntityList() {
        // Given : Test수행에 필요한 객체 및 데이터 설정
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("example-jpa");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        MemberEntity firstMemberEntity = MemberGenerator.buildMemberEntityByMemberNoAndMemberName(1L, "최용석");
        MemberEntity secondMemberEntity = MemberGenerator.buildMemberEntityByMemberNoAndMemberName(2L, "이성욱");
        MemberEntity thirdMemberEntity = MemberGenerator.buildMemberEntityByMemberNoAndMemberName(3L, "박재현");

        int offset = 0;
        int limit = 3;

        entityTransaction.begin();
        try {
            entityManager.persist(firstMemberEntity);
            entityManager.persist(secondMemberEntity);
            entityManager.persist(thirdMemberEntity);
            entityTransaction.commit();

            // When : JPQL을 이용한 MmemberEntity목록 조회 -> MemberEntity객체를 대상으로한 페이징 쿼리 생성 및 실횅
            List<MemberEntity> savedMemberEntityList = entityManager.createQuery("select member_entity from MemberEntity as member_entity", MemberEntity.class)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .getResultList()
            ;

            // Then : JPQL을 이용한 MemberEntity객체 목록 조회 여부 확인
            assertThat(savedMemberEntityList).contains(firstMemberEntity);
            assertThat(savedMemberEntityList).contains(secondMemberEntity);
            assertThat(savedMemberEntityList).contains(thirdMemberEntity);
        } catch (Exception e) {
            entityTransaction.rollback();
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
    }
}