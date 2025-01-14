// lib/models/recent_history_item.dart

import 'dart:convert';

/// RecentHistoryItem 클래스는 사용자의 최근 검색 내역을 나타냅니다.
/// 각 항목은 고유한 ID, 장소 ID, 장소 이름을 포함합니다.
class RecentHistoryItem {
  final String id;         // 최근 내역 항목의 고유 ID
  final String placeID;    // 장소의 고유 ID
  final String placeName;  // 장소의 이름

  RecentHistoryItem({
    required this.id,
    required this.placeID,
    required this.placeName,
  });

  /// JSON 데이터를 RecentHistoryItem 객체로 변환합니다.
  factory RecentHistoryItem.fromJson(Map<String, dynamic> json) {
    return RecentHistoryItem(
      id: json['id'] as String,
      placeID: json['placeID'] as String,
      placeName: json['placeName'] as String,
    );
  }

  /// RecentHistoryItem 객체를 JSON 형식으로 변환합니다.
  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'placeID': placeID,
      'placeName': placeName,
    };
  }

  /// JSON 문자열을 RecentHistoryItem 객체로 변환합니다.
  factory RecentHistoryItem.fromJsonString(String jsonString) {
    return RecentHistoryItem.fromJson(json.decode(jsonString));
  }

  /// RecentHistoryItem 객체를 JSON 문자열로 변환합니다.
  String toJsonString() {
    return json.encode(toJson());
  }

  @override
  String toString() {
    return 'RecentHistoryItem{id: $id, placeID: $placeID, placeName: $placeName}';
  }

  /// 동일한 RecentHistoryItem인지 비교합니다.
  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is RecentHistoryItem &&
          runtimeType == other.runtimeType &&
          id == other.id &&
          placeID == other.placeID &&
          placeName == other.placeName;

  @override
  int get hashCode => id.hashCode ^ placeID.hashCode ^ placeName.hashCode;
}
