package ex01;

import domain.MemberEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * @author : choi-ys
 * @date : 2021/03/15 2:29 오후
 * @Content : JPA 구동 예제
 */
public class HelloJPA {

    public static void main(String[] args) {
        /**
         * EntityManagerFactory : Entity로 선언된 Java Object를 관리하는 EntityManger 생성 부
         *  - Application 기동 시 jaxb-api 의존성에 의해 해석된 persistence.xml의 persistence-unit태그 내용을 기반으로 생성
         *  - persistence-unit 태그에 기술된 내용 : Datasource, JPA 구동 옵션
         */
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("example-jpa");

        /**
         * EntityManager : 해당 쓰레드에서 Entity로 선언된 Java Object와 DB table 연동을 관리하는 객체
         *  - Entity를 이용한 DB 연동에 관련된 모든 작업을 처리
         *  - DB연동이 필요한 시점에 Connection Pool에서 connection 1개를 할당 받음
         *  - 작업단위인 Transaction을 생성하여 작업 수행 이후 반드시 close하여 db 커넥션을 반환
         */
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        /**
         * EntityTransaction : 해당 쓰레드에서 Data변경 작업이 필요한 경우 EntityManger가 생성한 작업 단위
         *  - Data를 변경하는 모든 작업은 EntityManager가 생성한 Transaction단위 안에서만 가능
         */
        EntityTransaction entityTransaction = entityManager.getTransaction();

        /**
         * Application 기동 시 persistence.xml의 hibernate.hbm2ddl.auto 옵션 적용
         *  - 해당 Project에 명시된 Entity를 기준으로 DDL 스크립트 생성 및 실행
         *  - MemberEntity를 기준으로 생성 및 실행된 DDL 스크립트
         * create table member_tb (
         *    member_no bigint not null,
         *    member_name varchar(25) not null,
         *    primary key (member_no)
         * )
         */
        MemberEntity memberEntity = MemberEntity.builder()
                .memberNo(1L)
                .memberName("최용석")
                .build();

        /** Transaction 시작 */
        entityTransaction.begin();
        try {
            /** insert domain.MemberEntity
             * insert
             *   into
             *       member_tb
             *       (member_name, member_no)
             *   values
             *       (?, ?)
             * binding parameter [1] as [VARCHAR] - [최용석]
             * binding parameter [2] as [BIGINT] - [1]
             */
            entityManager.persist(memberEntity);

            /** Transaction 수행 */
            entityTransaction.commit();
        } catch (Exception e) {
            /** Transaction 실행취소 */
            entityTransaction.rollback();
            e.printStackTrace();
        } finally {
            /** DB 커넥션 반환 */
            entityManager.close();
            entityManagerFactory.close();
        }
    }
}