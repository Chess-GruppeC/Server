package at.aau.se2.server.mapper;

import at.aau.se2.server.entity.Player;
import at.aau.se2.server.dto.PlayerDTO;
import org.springframework.stereotype.Service;

@Service
public class PlayerMapperImpl implements PlayerMapper {

    @Override
    public PlayerDTO map(Player player) {
        return new PlayerDTO(player.getName(), player.getDiceValue());
    }
}
