// lib/components/recent_history.dart
import 'package:flutter/material.dart';
import '../models/path_model.dart';
import '../models/recent_history_item.dart';

class RecentHistory extends StatelessWidget {
  final List<PathModel> recentPaths;
  final List<RecentHistoryItem> recentHistory;
  final Function(String) onPathDelete;
  final Function(PathModel) onPathSelect;
  final Function(String) onHistoryDelete;
  final Function(RecentHistoryItem) onHistorySelect;

  const RecentHistory({
    Key? key,
    required this.recentPaths,
    required this.recentHistory,
    required this.onPathDelete,
    required this.onPathSelect,
    required this.onHistoryDelete,
    required this.onHistorySelect,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return ExpansionTile(
      title: Text('Recent History'),
      children: [
        // Recent Paths
        ListTile(
          title: Text('Recent Paths'),
        ),
        ...recentPaths.map((path) {
          return ListTile(
            title: Text('${path.departure} → ${path.arrival}'),
            trailing: IconButton(
              icon: Icon(Icons.delete, color: Colors.red),
              onPressed: () => onPathDelete(path.id),
            ),
            onTap: () => onPathSelect(path),
          );
        }).toList(),
        Divider(),
        // Recent History Items
        ListTile(
          title: Text('Recent Searches'),
        ),
        ...recentHistory.map((item) {
          return ListTile(
            title: Text(item.placeName),
            trailing: IconButton(
              icon: Icon(Icons.delete, color: Colors.red),
              onPressed: () => onHistoryDelete(item.id),
            ),
            onTap: () => onHistorySelect(item),
          );
        }).toList(),
      ],
    );
  }
}
