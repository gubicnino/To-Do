import React, { useEffect, useState } from "react";
import { PieChart, Pie, Tooltip, ResponsiveContainer, Legend, Cell } from "recharts";

import { getAnalytics } from "../../services/analytics";
import { loadUserFromStorage } from "../../services/auth";
import { getUserTodos } from "../../services/todos"; 

export default function Vizualizacija_podatkov() {
  const user = loadUserFromStorage();
  const userId = user?.id;

  const [stats, setStats] = useState(null);
  const [pieData, setPieData] = useState([]);
  const [err, setErr] = useState("");

  useEffect(() => {
    if (!userId) {
      setErr("Uporabnik ni prijavljen.");
      return;
    }

    Promise.all([getAnalytics(userId), getUserTodos(userId)])
      .then(([analytics, todos]) => {
        setStats(analytics);

        // PREŠTEJ PRIORITETE
        const counts = {};
        for (const t of todos) {
          const p = t.priority || "NONE";
          counts[p] = (counts[p] || 0) + 1;
        }

        // Pretvori v obliko za recharts
        const data = Object.entries(counts).map(([name, value]) => ({ name, value }));
        setPieData(data);
      })
      .catch((e) => {
        console.error(e);
        setErr(e?.response?.data?.message || "Napaka pri nalaganju analitike.");
      });
  }, [userId]);

  if (err) return <div style={{ padding: 24 }}>{err}</div>;
  if (!stats) return <div style={{ padding: 24 }}>Nalagam…</div>;

  return (
    <div style={{ padding: 24, display: "grid", gap: 16 }}>
      <h2 style={{ margin: 0 }}>Vizualizacija podatkov</h2>

      {/* KPI */}
      <div style={{ display: "grid", gridTemplateColumns: "repeat(3, 1fr)", gap: 12 }}>
        <Kpi title="Completion %" value={`${Math.round(stats.completionPercentage ?? 0)}%`} />
        <Kpi title="Aktivne naloge" value={stats.activeTasksCount ?? 0} />
        <Kpi title="Skupaj ur" value={fmt(stats.totalTimeSpent)} />

        <Kpi title="Povp. trajanje (h)" value={fmt(stats.averageDuration, true)} />
        <Kpi title="Ocena ur do konca" value={`${Number(stats.timeToCompleteAllTodos ?? 0).toFixed(1)} h`} />
        <Kpi title="Ocena delovnih dni" value={`${stats.workDaysToCompleteAllTodos ?? 0} dni`} />
      </div>

      {/* PIE graf */}
      <div style={{ border: "1px solid #e5e5e5", borderRadius: 12, padding: 12 }}>
        <div style={{ fontWeight: 700, marginBottom: 8 }}>Naloge po pomembnosti (Priority)</div>

        <div style={{ width: "100%", height: 320 }}>
          <ResponsiveContainer>
            <PieChart>
              <Pie data={pieData} dataKey="value" nameKey="name" outerRadius={110} label>
                {pieData.map((_, idx) => <Cell key={idx} />)}
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
