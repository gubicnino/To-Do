import api from "./api";

// tvoji OBSTOJEČI statistični podatki
export const getStats = (userId) =>
  api.get(`/todos/user/${userId}/analytics`).then(res => res.data);

// podatki za pie chart
export const getPieAnalytics = (userId) =>
  api.get(`/todos/user/${userId}/analytics/pie`).then(res => res.data);
