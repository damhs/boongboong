package com.example.shoong.service;

import com.example.shoong.dto.path.PathCreateRequest;
import com.example.shoong.dto.path.PathDTO;
import com.example.shoong.dto.path.PathUpdateRequest;
import com.example.shoong.dto.place.PlaceDTO;
import com.example.shoong.entity.Path;
import com.example.shoong.entity.Place;
import com.example.shoong.entity.User;
import com.example.shoong.exception.ResourceNotFoundException;
import com.example.shoong.repository.PathRepository;
import com.example.shoong.repository.PlaceRepository;
import com.example.shoong.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

@Service
public class PathService {

  private final PathRepository pathRepository;
  private final UserRepository userRepository;
  private final PlaceRepository placeRepository;

  public PathService(
      PathRepository pathRepository,
      UserRepository userRepository,
      PlaceRepository placeRepository) {
    this.pathRepository = pathRepository;
    this.userRepository = userRepository;
    this.placeRepository = placeRepository;
  }

  @Transactional(readOnly = true)
  public List<PathDTO> getPaths() {
    List<Path> paths = pathRepository.findAll();
    return paths.stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
  }

  @Transactional
  public PathDTO createOrUpdatePath(PathCreateRequest request) {
    // user, origin, destination 존재 여부 체크
    User user = userRepository.findById(request.getUserID())
        .orElseThrow(() -> new ResourceNotFoundException("유저가 존재하지 않습니다. ID=" + request.getUserID()));
    Place origin = placeRepository.findById(request.getOriginID())
        .orElseThrow(() -> new ResourceNotFoundException("출발지 장소가 없습니다. ID=" + request.getOriginID()));
    Place dest = placeRepository.findById(request.getDestinationID())
        .orElseThrow(() -> new ResourceNotFoundException("도착지 장소가 없습니다. ID=" + request.getDestinationID()));

    Optional<Path> existingPath = pathRepository.findByUser_UserIDAndOrigin_PlaceIDAndDestination_PlaceID(
        request.getUserID(), request.getOriginID(), request.getDestinationID());

    Path path;
    if (existingPath.isPresent()) {
      path = existingPath.get();
      path.setUpdatedAt(LocalDateTime.now());
    } else {
      path = new Path();
      path.setPathID(UUID.randomUUID().toString());
      path.setUser(user);
      path.setOrigin(origin);
      path.setDestination(dest);
      path.setTotalDistance(request.getTotalDistance());
      path.setTotalTime(request.getTotalTime());
      path.setUpdatedAt(LocalDateTime.now());
    }

    Path saved = pathRepository.save(path);
    return toDTO(saved);
  }

  @Transactional(readOnly = true)
  public PathDTO getPath(String pathID) {
    Path path = pathRepository.findById(pathID)
        .orElseThrow(() -> new ResourceNotFoundException("경로를 찾을 수 없습니다. ID=" + pathID));
    return toDTO(path);
  }

  @Transactional(readOnly = true)
  public List<PathDTO> getRecentPaths(String userID) {
    List<Path> paths = pathRepository.findTop10ByUser_UserIDOrderByUpdatedAtDesc(userID);
    return paths.stream()
        .map(path -> toDTO(path))
        .collect(Collectors.toList());
  }

  @Transactional
  public PathDTO updatePath(String pathID, PathUpdateRequest request) {
    Path path = pathRepository.findById(pathID)
        .orElseThrow(() -> new ResourceNotFoundException("경로를 찾을 수 없습니다. ID=" + pathID));

    if (request.getTotalDistance() != null)
      path.setTotalDistance(request.getTotalDistance());
    if (request.getTotalTime() != null)
      path.setTotalTime(request.getTotalTime());

    return toDTO(path);
  }

  @Transactional
  public void deletePath(String pathID) {
    if (!pathRepository.existsById(pathID)) {
      throw new ResourceNotFoundException("경로가 존재하지 않습니다. ID=" + pathID);
    }
    pathRepository.deleteById(pathID);
  }

  private PathDTO toDTO(Path path) {
    PathDTO dto = new PathDTO();
    dto.setPathID(path.getPathID());
    dto.setUserID(path.getUser().getUserID());
    dto.setOriginID(path.getOrigin().getPlaceID());
    dto.setDestinationID(path.getDestination().getPlaceID());
    dto.setTotalDistance(path.getTotalDistance());
    dto.setTotalTime(path.getTotalTime() == null ? null : path.getTotalTime().toString());
    dto.setUpdatedAt(path.getUpdatedAt() == null ? null : path.getUpdatedAt().toString());
    return dto;
  }
}
