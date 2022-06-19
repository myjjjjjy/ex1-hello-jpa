package dialect;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;

public class MyH2Dialect extends H2Dialect { // 내가 사용하는 dialect 상속 받기

    // 사용자 정의 함수 호출
    public MyH2Dialect(){
        registerFunction("group_concat", new StandardSQLFunction("group_concat", StandardSQLFunction.STRING));
        // registerFucntion("이름", 조건("")) -> persistence.xml에서 설정해주기
        // 외우지 말고 소스 코드 열어보고 참조해서 작성하기
    }
}
