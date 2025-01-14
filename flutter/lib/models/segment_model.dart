// lib/models/segment_model.dart
class SegmentModel {
  final String segmentID; // 세그먼트 고유 ID
  final int sequenceNumber; // 순서 번호
  final String description; // 지침 설명
  final int distance; // 거리 (미터 단위)
  final double? bearing; // 방위 (도 단위), null 가능
  final Duration duration; // 소요 시간

  SegmentModel({
    required this.segmentID,
    required this.sequenceNumber,
    required this.description,
    required this.distance,
    this.bearing,
    required this.duration,
  });

  // JSON에서 SegmentModel 객체로 변환
  factory SegmentModel.fromJson(Map<String, dynamic> json) {
    return SegmentModel(
      segmentID: json['segmentID'],
      sequenceNumber: json['sequenceNumber'],
      description: json['description'],
      distance: json['distance'],
      bearing: json['bearing'] != null
          ? (json['bearing'] as num).toDouble()
          : null,
      duration: Duration(seconds: json['durationSeconds'] ?? 0),
    );
  }

  // SegmentModel 객체를 JSON으로 변환
  Map<String, dynamic> toJson() => {
        'segmentID': segmentID,
        'sequenceNumber': sequenceNumber,
        'description': description,
        'distance': distance,
        'bearing': bearing,
        'durationSeconds': duration.inSeconds,
      };
}
