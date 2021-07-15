package kitchenpos.repository.ordertable;

import kitchenpos.application.ordertable.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findAllByIdIn(Iterable<Long> ids);
}