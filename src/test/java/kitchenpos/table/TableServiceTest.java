package kitchenpos.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.table.dao.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @InjectMocks
    TableService tableService;

    @Mock
    OrderDao orderDao;

    @Mock
    OrderTableRepository orderTableRepository;

    OrderTable 주문테이블;
    TableGroup 단체테이블;

    @BeforeEach
    void setUp() {
        주문테이블 = new OrderTable(2, false);
        주문테이블.setId(1L);
        단체테이블 = new TableGroup(Collections.singletonList(주문테이블));
    }

    @Test
    @DisplayName("주문에 대한 주문 테이블을 생성한다")
    void create() {
        // given
        given(orderTableRepository.save(any())).willReturn(주문테이블);
        OrderTableRequest 주문요청 = new OrderTableRequest(2, false);

        // when
        OrderTableResponse actual = tableService.create(주문요청);

        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getTableGroup()).isNull()
        );
    }

    @Test
    @DisplayName("주문 테이블을 조회한다")
    void list() {
        // given
        given(orderTableRepository.findAll()).willReturn(Collections.singletonList(주문테이블));

        // when
        List<OrderTableResponse> actual = tableService.list();

        // then
        assertThat(actual).hasSize(1);
    }

    @Test
    @DisplayName("주문 테이블을 빈 상태로 만든다")
    void changeEmpty() {
        // given
        OrderTableRequest 변경테이블 = new OrderTableRequest(주문테이블.getNumberOfGuests(), true);
        given(orderTableRepository.findById(any())).willReturn(Optional.ofNullable(주문테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(false);

        // when
        OrderTableResponse actual = tableService.changeEmpty(주문테이블.getId(), 변경테이블);

        // then
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 주문테이블이면 빈 상태로 변경 불가능하다")
    void changeEmpty_notExistOrderTable() {
        // given
        OrderTableRequest 변경테이블 = new OrderTableRequest(주문테이블.getNumberOfGuests(), 주문테이블.isEmpty());
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeEmpty(주문테이블.getId(), 변경테이블)
        );
    }

    @Test
    @DisplayName("단체 테이블에 속하면 빈 상태로 변경 불가능하다")
    void changeEmpty_notExistTableGroup() {
        // given
        주문테이블.setTableGroup(단체테이블);
        OrderTableRequest 변경테이블 = new OrderTableRequest(주문테이블.getNumberOfGuests(), 주문테이블.isEmpty(), 단체테이블);
        given(orderTableRepository.findById(any())).willReturn(Optional.ofNullable(주문테이블));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeEmpty(주문테이블.getId(), 변경테이블)
        );
    }

    @Test
    @DisplayName("현재 주문 상태가 계산 완료가 아닌 경우 테이블을 빈 상태로 변경 불가능하다")
    void changeEmpty_orderStatus_completion() {
        // given
        OrderTableRequest 변경테이블 = new OrderTableRequest(주문테이블.getNumberOfGuests(), 주문테이블.isEmpty());
        given(orderTableRepository.findById(any())).willReturn(Optional.ofNullable(주문테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(),
                any())).willReturn(true);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeEmpty(주문테이블.getId(), 변경테이블)
        );
    }

    @Test
    @DisplayName("방문한 손님 수 정보를 변경한다")
    void changeNumberOfGuests() {
        // given
        OrderTableRequest 변경테이블 = new OrderTableRequest(5, true);
        given(orderTableRepository.findById(any())).willReturn(Optional.ofNullable(주문테이블));

        // when
        OrderTableResponse actual = tableService.changeNumberOfGuests(주문테이블.getId(), 변경테이블);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(변경테이블.getNumberOfGuests());
    }

    @Test
    @DisplayName("변경하려는 방문 손님 수는 0명이상이다")
    void changeNumberOfGuests_numberError() {
        // given
        OrderTableRequest 변경테이블 = new OrderTableRequest(-1, true);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeNumberOfGuests(주문테이블.getId(), 변경테이블)
        );
    }

    @Test
    @DisplayName("변경대상인 주문테이블은 존재하는 주문테이블이어야 한다")
    void changeNumberOfGuests_orderTableError() {
        // given
        OrderTableRequest 변경테이블 = new OrderTableRequest(5, true);
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeNumberOfGuests(주문테이블.getId(), 변경테이블)
        );
    }

    @Test
    @DisplayName("변경대상인 주문테이블은 비어있지 않은 주문테이블이다")
    void changeNumberOfGuests_emptyTableError() {
        // given
        주문테이블.setEmpty(true);
        OrderTableRequest 변경테이블 = new OrderTableRequest(2, true);
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeNumberOfGuests(주문테이블.getId(), 변경테이블)
        );
    }
}
