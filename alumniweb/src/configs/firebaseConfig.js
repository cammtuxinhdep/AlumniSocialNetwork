import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
import { getDatabase } from "firebase/database";

const firebaseConfig = {
  apiKey: "AIzaSyDf9p8Dfy0UVoUHQxm2lvWnjhITahTe90U",
  authDomain: "alumnisocialnetwork-e7a93.firebaseapp.com",
  projectId: "alumnisocialnetwork-e7a93",
  storageBucket: "alumnisocialnetwork-e7a93.firebasestorage.app",
  messagingSenderId: "651917155207",
  appId: "1:651917155207:web:6510f7301ebf712eb41122",
  measurementId: "G-JCP5STKFGV"
};

const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);
export const db = getDatabase(app);