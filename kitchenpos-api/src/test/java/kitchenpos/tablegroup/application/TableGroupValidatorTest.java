package kitchenpos.tablegroup.application;

import kitchenpos.tablegroup.application.TableGroupValidator;
import kitchenpos.common.exception.table.IllegalOrderTableException;
import kitchenpos.common.exception.tablegroup.NotSupportUngroupException;
import kitchenpos.table.application.TableValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static domain.table.OrderTableFixtures.주문가능_두명테이블;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

/**
 * packageName : kitchenpos.tablegroup.application
 * fileName : TableGroupValidator
 * author : haedoang
 * date : 2021-12-27
 * description :
 */
@DisplayName("테이블그룹 Validator 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupValidatorTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableValidator tableValidator;

    @InjectMocks
    private TableGroupValidator tableGroupValidator;

    @Test
    @DisplayName("테이블이 사용가능인 경우 그룹 테이블로 사용할 수 없다.")
    public void createFailByTableStatus() {
        // given
        given(orderTableRepository.findAllById(anyList())).willReturn(Lists.newArrayList(주문가능_두명테이블(), 주문가능_두명테이블()));

        //then
        assertThatThrownBy(() -> tableGroupValidator.validateGroup(anyList()))
                .isInstanceOf(IllegalOrderTableException.class);
    }

    @Test
    @DisplayName("테이블 개수가 2개보다 작은 경우 등록할 수 없다.")
    public void createFailByTables() {
        // given
        given(orderTableRepository.findAllById(anyList())).willReturn(Lists.newArrayList(주문가능_두명테이블()));

        // then
        assertThatThrownBy(() -> tableGroupValidator.validateGroup(anyList()))
                .isInstanceOf(IllegalOrderTableException.class);
    }

    @Test
    @DisplayName("등록되지 않은 테이블인 경우 그룹화 할 수 없다.")
    public void createFailByUnknownTable() {
        // given
        given(orderTableRepository.findAllById(anyList())).willReturn(Collections.emptyList());

        // then
        assertThatThrownBy(() -> tableGroupValidator.validateGroup(anyList()))
                .isInstanceOf(IllegalOrderTableException.class);
    }

    @Test
    @DisplayName("테이블이 비워있지 않으면 등록할 수 없다.")
    public void createFailByUsingTable() {
        // given
        given(orderTableRepository.findAllById(anyList())).willReturn(Lists.newArrayList(주문가능_두명테이블(), 주문가능_두명테이블()));

        // then
        assertThatThrownBy(() -> tableGroupValidator.validateGroup(anyList()))
                .isInstanceOf(IllegalOrderTableException.class);
    }

    @Test
    @DisplayName("테이블의 주문 상태가 조리, 식사중인 경우 그룹화를 해제할 수 없다.")
    public void ungroupFail() {
        // given
        given(orderTableRepository.findAllByTableGroupId(any())).willReturn(Lists.newArrayList(주문가능_두명테이블(), 주문가능_두명테이블()));
        given(tableValidator.usingTable(any(OrderTable.class))).willReturn(true);

        // then
        assertThatThrownBy(() -> tableGroupValidator.validateUngroup(new TableGroup())).isInstanceOf(NotSupportUngroupException.class);
    }
}