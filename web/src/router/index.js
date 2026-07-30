import { createRouter, createWebHistory } from 'vue-router';
import AboutPage from '../views/AboutPage.vue';
import ExerciseCatalogPage from '../views/ExerciseCatalogPage.vue';
import ExerciseDetailPage from '../views/ExerciseDetailPage.vue';
import ExercisePracticePage from '../views/ExercisePracticePage.vue';
import HomePage from '../views/HomePage.vue';
import NotFoundPage from '../views/NotFoundPage.vue';
import ChallengeSessionPage from '../views/ChallengeSessionPage.vue';
import SubmissionPage from '../views/SubmissionPage.vue';

const routes = [
  { path: '/', name: 'home', component: HomePage },
  { path: '/exercises', name: 'exercises', component: ExerciseCatalogPage },
  { path: '/exercises/:id', name: 'exercise-detail', component: ExerciseDetailPage, props: true },
  { path: '/exercises/:id/practice', name: 'exercise-practice', component: ExercisePracticePage, props: true },
  { path: '/submissions/:id', name: 'submission-detail', component: SubmissionPage, props: true },
  { path: '/challenge-sessions/:id', name: 'challenge-session-detail', component: ChallengeSessionPage, props: true },
  { path: '/about', name: 'about', component: AboutPage },
  { path: '/:pathMatch(.*)*', name: 'not-found', component: NotFoundPage }
];

export default createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 };
  }
});
