import React, { useEffect, useState } from "react";
import { PieChart, Pie, Tooltip, ResponsiveContainer, Legend, Cell } from "recharts";

import { getStats, getPieAnalytics } from "../../services/analytics";
import { loadUserFromStorage } from "../../services/auth";

export default function Vizualizacija_podatkov() {
  const user = loadUserFromStorage();
  const userId = user?.id;

  const [stats, setStats] = useState(null);
  const [pie, setPie] = useState(null);
  const [err, setErr] = useState("");

  useEffect(() => {
    if (!userId) {
      setErr("Uporabnik ni prijavljen.");
      return;
    }

    Promise.all([getStats(userId), getPieAnalytics(userId)])
      .then(([statsRes, pieRes]) => {
        setStats(statsRes);
        setPie(pieRes);
      })
      .catch((e) => {
        console.error(e);
        setErr(e?.response?.data?.message || "Napaka pri nalaganju analitike.");
      });
  }, [userId]);

  if (err) return <div style={{ padding: 24 }}>{err}</div>;
  if (!stats || !pie) return <div style={{ padding: 24 }}>Nalagam…</div>;

  return (
    <div style={{ padding: 24, display: "grid", gap: 16 }}>
      <h2 style={{ margin: 0 }}>Vizualizacija podatkov</h2>

      {/* KPI (številke iz /analytics) */}
      <div style={{ display: "grid", gridTemplateColumns: "repeat(4, 1fr)", gap: 12 }}>
        <Kpi title="Completion %" value={`${Math.round(stats.completionPercentage)}%`} />
        <Kpi title="Aktivne naloge" value={stats.activeTasksCount} />
        <Kpi title="Skupaj ur" value={fmt(stats.totalTimeSpent)} />
        <Kpi title="Povp. trajanje (h)" value={fmt(stats.averageDuration, true)} />
      </div>

      {/* PIE (iz /analytics/pie) */}
      <div style={{ border: "1px solid #e5e5e5", borderRadius: 12, padding: 12 }}>
        <div style={{ fontWeight: 700, marginBottom: 8 }}>Prioritete (pie chart)</div>

        <div style={{ display: "grid", gridTemplateColumns: "repeat(3, 1fr)", gap: 12, marginBottom: 12 }}>
          <Kpi title="Skupaj nalog" value={pie.total} />
          <Kpi title="Zaključene" value={pie.completed} />
          <Kpi title="Aktivne" value={pie.active} />
        </div>

        <div style={{ width: "100%", height: 320 }}>
          <ResponsiveContainer>
            <PieChart>
              <Pie data={pie.byPriority || []} dataKey="value" nameKey="name" outerRadius={110} label>
                {(pie.byPriority || []).map((_, idx) => <Cell key={idx} />)}
              </Pie>
              <Tooltip />
              <Legend />
            </PieChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
}

function Kpi({ title, value }) {
  return (
    <div style={{ border: "1px solid #e5e5e5", borderRadius: 12, padding: 12 }}>
      <div style={{ fontSize: 12, opacity: 0.7 }}>{title}</div>
      <div style={{ fontSize: 22, fontWeight: 700 }}>{value}</div>
    </div>
  );
}

function fmt(x, dashWhenNull = false) {
  if (x === null || x === undefined) return dashWhenNull ? "—" : "0.00";
  if (typeof x === "number") return x.toFixed(2);
  return String(x);
}
