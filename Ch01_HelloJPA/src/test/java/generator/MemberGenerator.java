package generator;


import domain.MemberEntity;

/**
 * @author : choi-ys
 * @date : 2021/03/15 4:35 오후
 * @Content : 각 TC에서 사용할 MemberEntity 생성 부
 */
public class MemberGenerator {

    private static long defaultMemberNo = 1L;
    private static String defaultMemberName = "최용석";

    public static MemberEntity buildMemberEntity(){
        return getMemberEntity(defaultMemberNo, defaultMemberName);
    }

    public static MemberEntity buildMemberEntityByMemberNoAndMemberName(long memberNo, String memberName){
        return getMemberEntity(memberNo, memberName);
    }

    private static MemberEntity getMemberEntity(long memberNo, String memberName){
        return MemberEntity.builder()
                .memberNo(memberNo)
                .memberName(memberName)
                .build();
    }
}