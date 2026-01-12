import api from "./api";

export const getAnalytics = (userId) =>
  api.get(`/todos/user/${userId}/analytics`).then(res => res.data);
