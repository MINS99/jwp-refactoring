package kitchenpos.table.dao;

import kitchenpos.table.domain.TableGroup;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
}
