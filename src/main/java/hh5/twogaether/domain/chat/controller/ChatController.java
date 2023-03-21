package hh5.twogaether.domain.chat.controller;

import hh5.twogaether.domain.chat.dto.ChatMessageDto;
import hh5.twogaether.domain.chat.dto.ChatRoomRealResponseDto;
import hh5.twogaether.domain.chat.entity.ChatMessage;
import hh5.twogaether.domain.chat.entity.ChatRoom;
import hh5.twogaether.domain.chat.repository.ChatMessageRepository;
import hh5.twogaether.domain.chat.repository.ChatRoomRepository;
import hh5.twogaether.domain.users.entity.User;
import hh5.twogaether.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    @MessageMapping("/chat/message")
//    @SendTo("/topic/public")
    public void message(@Payload ChatMessageDto chatMessageDto) {
        User user = userRepository.findByUsername(chatMessageDto.getSender())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        ChatRoom chatRoom = chatRoomRepository.findByRoomId(chatMessageDto.getRoomId());

        ChatMessage chatMessage = new ChatMessage(chatRoom.getRoomId(), user, chatMessageDto.getMessage());
        chatMessageRepository.save(chatMessage);

        ChatRoomRealResponseDto chatRoomRealResponseDto = new ChatRoomRealResponseDto(chatMessage);

//        chatRoom.ChatRoomLastMessage(chatMessageDto.getMessage());

        messagingTemplate.convertAndSend("/sub/chat/rooms/" + chatMessageDto.getRoomId(), chatRoomRealResponseDto);
    }

}
