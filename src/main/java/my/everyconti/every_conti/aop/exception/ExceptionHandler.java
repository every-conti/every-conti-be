//package my.everyconti.every_conti.common.aop.exception;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//
//@Component
//@Aspect
//public class ExceptionHandler {
////    @Around("execution(* my.everyconti.every_conti..*(..))")
//    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
//    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
//        try {
//            return joinPoint.proceed(); // 원래 로직 실행
//        } catch (Exception e) {
//            // 예외 로깅
//            System.err.println("⚠️ 예외 발생: " + e.getClass().getSimpleName() + " - " + e.getMessage());
//
//            // 예외 처리 방식 선택:
//            // 1. 예외 던지기
//            throw e;
//
//            // 2. 예외를 응답으로 바꾸기 (선택사항 - 아래는 컨트롤러에서만 가능)
////            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예외 발생: " + e.getMessage());
//        }
//    }
//}
