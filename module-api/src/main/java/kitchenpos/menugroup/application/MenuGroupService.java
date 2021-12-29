package kitchenpos.menugroup.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupCreateRequest;
import kitchenpos.menugroup.dto.MenuGroupDto;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {
	private final MenuGroupRepository menuGroupRepository;

	public MenuGroupService(MenuGroupRepository menuGroupRepository) {
		this.menuGroupRepository = menuGroupRepository;
	}

	@Transactional
	public MenuGroupDto create(MenuGroupCreateRequest request) {
		MenuGroup menuGroup = menuGroupRepository.save(request.toMenuGroup());
		return MenuGroupDto.from(menuGroup);
	}

	public List<MenuGroupDto> list() {
		List<MenuGroup> menuGroups = menuGroupRepository.findAll();

		return menuGroups.stream()
			.map(MenuGroupDto::from)
			.collect(Collectors.toList());
	}
}