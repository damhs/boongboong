// lib/models/path_model.dart
import 'segment_model.dart';

class PathModel {
  final String id; // 고유 식별자 (예: 데이터베이스의 ID)
  final String pathID; // 경로 식별자 (백엔드에서 사용하는 ID)
  final String userID; // 사용자 식별자
  final String originID; // 출발지 장소 ID
  final String destinationID; // 도착지 장소 ID
  final List<SegmentModel> segments; // 경로에 포함된 세그먼트 리스트
  final DateTime createdAt; // 경로 생성 시간

  PathModel({
    required this.id,
    required this.pathID,
    required this.userID,
    required this.originID,
    required this.destinationID,
    required this.segments,
    required this.createdAt,
  });

  // JSON에서 PathModel 객체로 변환
  factory PathModel.fromJson(Map<String, dynamic> json) {
    var segmentsFromJson = json['segments'] as List<dynamic>? ?? [];
    List<SegmentModel> segmentList = segmentsFromJson
        .map((segmentJson) => SegmentModel.fromJson(segmentJson))
        .toList();

    return PathModel(
      id: json['id'],
      pathID: json['pathID'],
      userID: json['userID'],
      originID: json['originID'],
      destinationID: json['destinationID'],
      segments: segmentList,
      createdAt: DateTime.parse(json['createdAt']),
    );
  }

  // PathModel 객체를 JSON으로 변환
  Map<String, dynamic> toJson() => {
        'id': id,
        'pathID': pathID,
        'userID': userID,
        'originID': originID,
        'destinationID': destinationID,
        'segments': segments.map((segment) => segment.toJson()).toList(),
        'createdAt': createdAt.toIso8601String(),
      };
}
