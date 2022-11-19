package com.ll.exam.finalproject.app.rebate.service;

import com.ll.exam.finalproject.app.base.dto.RsData;
import com.ll.exam.finalproject.app.cash.entity.CashLog;
import com.ll.exam.finalproject.app.member.service.MemberService;
import com.ll.exam.finalproject.app.order.service.OrderService;
import com.ll.exam.finalproject.app.orderitem.entity.OrderItem;
import com.ll.exam.finalproject.app.rebate.entity.RebateOrderItem;
import com.ll.exam.finalproject.app.rebate.repository.RebateRepository;
import com.ll.exam.finalproject.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RebateService {
    private final RebateRepository rebateRepository;
    private final OrderService orderService;
    private final MemberService memberService;

    @Transactional
    public void makeDate(String yearMonth) {
        int monthEndDay = Ut.date.getEndDayOf(yearMonth);

        String fromDateStr = yearMonth + "-01 00:00:00.000000";
        String toDateStr = yearMonth + "-%02d 23:59:59.999999".formatted(monthEndDay);
        LocalDateTime fromDate = Ut.date.parse(fromDateStr);
        LocalDateTime toDate = Ut.date.parse(toDateStr);

        // 데이터 가져오기
        List<OrderItem> orderItems = orderService.findAllByPayDateBetweenOrderByIdAsc(fromDate, toDate);

        // 변환하기
        List<RebateOrderItem> rebateOrderItems = orderItems
                .stream()
                .map(this::toRebateOrderItem)
                .collect(Collectors.toList());

        // 저장하기
        rebateOrderItems.forEach(this::makeRebateOrderItem);
    }

    @Transactional
    public void makeRebateOrderItem(RebateOrderItem item) {
        RebateOrderItem oldRebateOrderItem = rebateRepository.findByOrderItemId(item.getOrderItem().getId()).orElse(null);

//        if (oldRebateOrderItem != null) {
//            rebateRepository.delete(oldRebateOrderItem);
//        }
        if (oldRebateOrderItem == null) {
            rebateRepository.save(item);
        }

    }

    public RebateOrderItem toRebateOrderItem(OrderItem orderItem) {
        return new RebateOrderItem(orderItem);
    }

    public List<RebateOrderItem> findRebateOrderItemsByPayDateIn(String yearMonth) {
        int monthEndDay = Ut.date.getEndDayOf(yearMonth);

        String fromDateStr = yearMonth + "-01 00:00:00.000000";
        String toDateStr = yearMonth + "-%02d 23:59:59.999999".formatted(monthEndDay);
        LocalDateTime fromDate = Ut.date.parse(fromDateStr);
        LocalDateTime toDate = Ut.date.parse(toDateStr);

        return rebateRepository.findAllByPayDateBetweenOrderByIdAsc(fromDate, toDate);
    }

    @Transactional
    public RsData rebate(long orderItemId) {
        RebateOrderItem rebateOrderItem = rebateRepository.findByOrderItemId(orderItemId).get();

        if (rebateOrderItem.isRebateAvailable() == false) {
            return RsData.of("F-1", "정산을 할 수 없는 상태입니다.");
        }

        int calculateRebatePrice = rebateOrderItem.calculateRebatePrice();

        RsData<Map<String, Object>> addCashRsData = memberService.addCash(
                rebateOrderItem.getProduct().getAuthor(),
                calculateRebatePrice,
                rebateOrderItem,
                CashLog.EvenType.작가정산__예치금);
        CashLog cashLog = (CashLog) addCashRsData.getData().get("cashLog");

        rebateOrderItem.setRebateDone(cashLog.getId());

        return RsData.of(
                "S-1",
                "정산성공",
                Ut.mapOf(
                        "cashLogId", cashLog.getId()
                )
        );
    }
}
