package my.everyconti.every_conti.conti;

import jakarta.transaction.Transactional;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.modules.conti.ContiService;
import my.everyconti.every_conti.modules.conti.dto.response.ContiDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@Transactional
public class ContiServiceTest {

    @Autowired
    private ContiService contiService;
    @Autowired
    private HashIdUtil hashIdUtil;

//    @Test
//    public void contiSaveTest(){
//        CreateContiDto dto = CreateContiDto.builder()
//                .title("0717콘티")
//                .memberId(hashIdUtil.encode(4L))
//                .date(LocalDate.now())
//                .build();
//
//        ContiDto result = contiService.createConti(dto);
//        System.out.println("result = " + result);
//    }

//    @Test
//    public void contiGetTest(){
//        ContiDto result = contiService.getContiDetail("MvbmOeYA");
//        System.out.println("result = " + result);
//    }

    @Test
//    @Commit
    public void addSongToContiTest(){
        String contiId = hashIdUtil.encode(4L);
        String songId = hashIdUtil.encode(23L);
        System.out.println("contiId = " + contiId);
        System.out.println("songId = " + songId);

        ContiDto conti =  contiService.addSongToConti(contiId, songId);
        System.out.println("conti = " + conti);
    }

//    @Test
//    public void getContiDetail(){
//        String contiId = hashIdUtil.encode(4L);
//        ContiDto contiDetail = contiService.getContiDetail(contiId);
//        System.out.println("contiDetail = " + contiDetail);
//    }

//    @Test
//    @Commit
//    public void updateContiSongOrder(){
//        List<String> contiSongIds = List.of(hashIdUtil.encode(2L), hashIdUtil.encode(15L), hashIdUtil.encode(1L));
//        ContiDto contiDto = contiService.updatecontiOrder(hashIdUtil.encode(4L), UpdateContiOrderDto.builder().contiSongIds(contiSongIds).build());
//        System.out.println("contiDto = " + contiDto);
//    }
}
