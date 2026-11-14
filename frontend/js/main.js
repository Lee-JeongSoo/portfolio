(function () {
  'use strict';

  const config = window.PORTFOLIO_CONFIG;
  const prefersReducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;

  const $ = (sel, root = document) => root.querySelector(sel);
  const $$ = (sel, root = document) => Array.from(root.querySelectorAll(sel));
  const sleep = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

  function storageGet(storage, key) {
    try { return storage.getItem(key); } catch (e) { return null; }
  }
  function storageSet(storage, key, value) {
    try { storage.setItem(key, value); } catch (e) { /* 저장소를 쓸 수 없는 환경이면 무시 */ }
  }

  /* =========================================================
     API helper
     ========================================================= */
  class ApiError extends Error {
    constructor(status, body) {
      super((body && body.message) || `HTTP ${status}`);
      this.status = status;
      this.body = body;
    }
  }

  async function api(path, { method = 'GET', body, timeout = config.coldStartTimeoutMs } = {}) {
    const controller = new AbortController();
    const timer = setTimeout(() => controller.abort(), timeout);
    try {
      const res = await fetch(config.apiBaseUrl + path, {
        method,
        headers: body ? { 'Content-Type': 'application/json' } : undefined,
        body: body ? JSON.stringify(body) : undefined,
        signal: controller.signal,
      });
      const data = await res.json().catch(() => null);
      if (!res.ok) throw new ApiError(res.status, data);
      return data;
    } finally {
      clearTimeout(timer);
    }
  }

  /* =========================================================
     API 상태 표시 (푸터)
     ========================================================= */
  const statusEl = $('#api-status');
  function setApiStatus(state, text) {
    statusEl.dataset.state = state;
    $('.api-status-text', statusEl).textContent = text;
  }

  /* =========================================================
     Theme
     ========================================================= */
  function initTheme() {
    const root = document.documentElement;
    const media = window.matchMedia('(prefers-color-scheme: dark)');
    const current = () => root.dataset.theme || (media.matches ? 'dark' : 'light');
    const btn = $('#theme-toggle');

    const syncLabel = () => {
      btn.setAttribute('aria-label', current() === 'dark' ? '라이트 모드로 전환' : '다크 모드로 전환');
    };
    btn.addEventListener('click', () => {
      const next = current() === 'dark' ? 'light' : 'dark';
      root.dataset.theme = next;
      storageSet(localStorage, 'theme', next);
      syncLabel();
    });
    media.addEventListener('change', syncLabel);
    syncLabel();
  }

  /* =========================================================
     Header: 스크롤 진행바, 그림자, 모바일 메뉴, 현재 섹션 표시
     ========================================================= */
  function initHeader() {
    const header = $('.site-header');
    const progress = $('#scroll-progress');
    let ticking = false;

    const update = () => {
      const max = document.documentElement.scrollHeight - window.innerHeight;
      const ratio = max > 0 ? window.scrollY / max : 0;
      progress.style.transform = `scaleX(${ratio})`;
      header.classList.toggle('scrolled', window.scrollY > 8);
      ticking = false;
    };
    window.addEventListener('scroll', () => {
      if (!ticking) {
        ticking = true;
        requestAnimationFrame(update);
      }
    }, { passive: true });
    update();

    // 모바일 메뉴
    const menuBtn = $('#menu-toggle');
    const links = $('#nav-links');
    const setOpen = (open) => {
      links.classList.toggle('open', open);
      menuBtn.setAttribute('aria-expanded', String(open));
      menuBtn.setAttribute('aria-label', open ? '메뉴 닫기' : '메뉴 열기');
    };
    menuBtn.addEventListener('click', () => setOpen(!links.classList.contains('open')));
    links.addEventListener('click', (e) => { if (e.target.closest('a')) setOpen(false); });
    document.addEventListener('keydown', (e) => { if (e.key === 'Escape') setOpen(false); });

    // 현재 보고 있는 섹션을 메뉴에 표시
    const navLinks = $$('.nav-links a');
    const observer = new IntersectionObserver((entries) => {
      entries.forEach((entry) => {
        if (!entry.isIntersecting) return;
        navLinks.forEach((a) => a.classList.toggle('active', a.getAttribute('href') === '#' + entry.target.id));
      });
    }, { rootMargin: '-45% 0px -50% 0px' });
    $$('main section[id]').forEach((s) => observer.observe(s));
  }

  /* =========================================================
     Typing effect
     ========================================================= */
  async function initTyping() {
    const el = $('#typing');
    const words = config.typingWords;
    if (prefersReducedMotion) {
      el.textContent = words[0];
      return;
    }
    let i = 0;
    for (;;) {
      const word = words[i % words.length];
      for (let n = 1; n <= word.length; n++) {
        el.textContent = word.slice(0, n);
        await sleep(70 + Math.random() * 50);
      }
      await sleep(1800);
      for (let n = word.length; n >= 0; n--) {
        el.textContent = word.slice(0, n);
        await sleep(35);
      }
      await sleep(350);
      i++;
    }
  }

  /* =========================================================
     Scroll reveal + 숫자 카운터
     ========================================================= */
  function initReveal() {
    // 같은 부모 안의 요소들은 순서대로 조금씩 늦게 나타나도록
    const groups = new Map();
    $$('.reveal').forEach((el) => {
      const list = groups.get(el.parentElement) || [];
      list.push(el);
      groups.set(el.parentElement, list);
    });
    groups.forEach((list) => list.forEach((el, idx) => el.style.setProperty('--delay', `${idx * 0.08}s`)));

    const pending = new Set($$('.reveal'));
    const show = (el) => {
      if (!pending.delete(el)) return;
      el.classList.add('visible');
      $$('.counter', el).forEach(animateCounter);
      observer.unobserve(el);
    };

    const observer = new IntersectionObserver((entries) => {
      entries.forEach((entry) => { if (entry.isIntersecting) show(entry.target); });
    }, { threshold: 0.12, rootMargin: '0px 0px -40px 0px' });
    pending.forEach((el) => observer.observe(el));

    // 빠르게 스크롤하거나 메뉴로 점프하면 observer가 요소를 건너뛸 수 있어서,
    // 이미 화면 위로 지나간 요소도 보이도록 처리한다.
    let ticking = false;
    const sweep = () => {
      pending.forEach((el) => { if (el.getBoundingClientRect().top < window.innerHeight) show(el); });
      ticking = false;
      if (!pending.size) window.removeEventListener('scroll', onScroll);
    };
    const onScroll = () => {
      if (!ticking) { ticking = true; requestAnimationFrame(sweep); }
    };
    window.addEventListener('scroll', onScroll, { passive: true });
    window.addEventListener('beforeprint', () => pending.forEach(show));
  }

  function animateCounter(el) {
    const target = Number(el.dataset.target);
    const suffix = el.dataset.suffix || '';
    if (prefersReducedMotion) {
      el.textContent = target + suffix;
      return;
    }
    const duration = 1400;
    const start = performance.now();
    const step = (now) => {
      const t = Math.min((now - start) / duration, 1);
      const eased = 1 - Math.pow(1 - t, 3);
      el.textContent = Math.round(target * eased) + suffix;
      if (t < 1) requestAnimationFrame(step);
    };
    requestAnimationFrame(step);
  }

  /* =========================================================
     Projects
     ========================================================= */
  const grid = $('#projects-grid');
  const filtersEl = $('#project-filters');
  let projects = [];
  let activeFilter = null;

  function safeUrl(url) {
    try {
      const u = new URL(url);
      return u.protocol === 'https:' || u.protocol === 'http:' ? u.href : null;
    } catch (e) {
      return null;
    }
  }

  function el(tag, props = {}, children = []) {
    const node = document.createElement(tag);
    Object.entries(props).forEach(([k, v]) => {
      if (k === 'text') node.textContent = v;
      else if (k === 'class') node.className = v;
      else node.setAttribute(k, v);
    });
    children.forEach((c) => c && node.append(c));
    return node;
  }

  function projectCard(p, index) {
    const links = [];
    const github = safeUrl(p.githubUrl);
    const demo = safeUrl(p.demoUrl);
    if (github) links.push(linkWithArrow(github, 'GitHub'));
    if (demo) links.push(linkWithArrow(demo, 'Live Demo'));

    const card = el('article', { class: 'project-card' }, [
      el('div', { class: 'project-head' }, [
        el('h3', { text: p.title }),
        p.featured ? el('span', { class: 'badge', text: '대표' }) : null,
      ]),
      el('p', { text: p.description }),
      el('ul', { class: 'tags', 'aria-label': '사용 기술' }, (p.techStack || []).map((t) => el('li', { text: t }))),
      links.length ? el('div', { class: 'project-links' }, links) : null,
    ]);
    card.style.animationDelay = `${index * 0.07}s`;
    return card;
  }

  function linkWithArrow(href, label) {
    return el('a', { href, target: '_blank', rel: 'noopener' }, [
      document.createTextNode(label + ' '),
      el('span', { class: 'arrow', 'aria-hidden': 'true', text: '→' }),
    ]);
  }

  function renderFilters() {
    // 2개 이상의 프로젝트에서 쓰인 기술만 필터로 노출 (많이 쓰인 순)
    const counts = new Map();
    projects.forEach((p) => (p.techStack || []).forEach((t) => counts.set(t, (counts.get(t) || 0) + 1)));
    const techs = [...counts.entries()].filter(([, c]) => c >= 2).sort((a, b) => b[1] - a[1]).map(([t]) => t);
    if (activeFilter && !techs.includes(activeFilter)) activeFilter = null;

    filtersEl.replaceChildren(...[null, ...techs].map((tech) => {
      const btn = el('button', {
        type: 'button',
        class: 'filter-btn',
        'aria-pressed': String(tech === activeFilter),
        text: tech || '전체',
      });
      btn.addEventListener('click', () => {
        activeFilter = tech;
        renderFilters();
        renderProjects();
      });
      return btn;
    }));
  }

  function renderProjects() {
    const list = activeFilter
      ? projects.filter((p) => (p.techStack || []).includes(activeFilter))
      : projects;
    grid.replaceChildren(...(list.length
      ? list.map(projectCard)
      : [el('p', { class: 'empty', text: '해당 기술을 사용한 프로젝트가 없습니다.' })]));
  }

  function showProjects(data) {
    projects = data;
    renderFilters();
    renderProjects();
  }

  async function initProjects() {
    const fromApi = api('/api/projects');
    // 너무 오래 기다리지 않도록: 일정 시간 안에 응답이 없으면 내장 데이터로 먼저 보여준다.
    const first = await Promise.race([
      fromApi.then((data) => data, () => null),
      sleep(config.initialWaitMs).then(() => null),
    ]);
    if (first) {
      showProjects(first);
      return;
    }
    showProjects(window.FALLBACK_PROJECTS);
    try {
      const data = await fromApi;
      // 서버가 늦게 깨어났고 내용이 다를 때만 다시 그린다 (불필요한 화면 깜빡임 방지)
      if (JSON.stringify(data) !== JSON.stringify(window.FALLBACK_PROJECTS)) showProjects(data);
    } catch (e) {
      /* 내장 데이터 그대로 사용 */
    }
  }

  /* =========================================================
     API 연결 확인 + 방문자 수
     ========================================================= */
  async function initStats() {
    const slowTimer = setTimeout(() => setApiStatus('checking', '서버를 깨우는 중…'), config.initialWaitMs);
    try {
      // 같은 탭에서 새로고침할 때마다 방문 수가 늘지 않도록 세션당 한 번만 기록
      const counted = storageGet(sessionStorage, 'visit-counted');
      const stats = counted ? await api('/api/stats') : await api('/api/stats/visit', { method: 'POST' });
      storageSet(sessionStorage, 'visit-counted', '1');
      setApiStatus('online', 'Spring Boot API 연결됨');
      $('#visits').textContent = `방문 ${Number(stats.visits).toLocaleString('ko-KR')}회`;
    } catch (e) {
      setApiStatus('offline', '오프라인 모드 (내장 데이터)');
    } finally {
      clearTimeout(slowTimer);
    }
  }

  /* =========================================================
     Contact form
     ========================================================= */
  const EMAIL_RE = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  function validateContact(values) {
    const errors = {};
    if (!values.name) errors.name = '이름을 입력해 주세요.';
    if (!values.email) errors.email = '이메일을 입력해 주세요.';
    else if (!EMAIL_RE.test(values.email)) errors.email = '올바른 이메일 형식이 아닙니다.';
    if (!values.message) errors.message = '메시지를 입력해 주세요.';
    else if (values.message.length < 10) errors.message = '메시지는 10자 이상 입력해 주세요.';
    return errors;
  }

  function initContact() {
    const form = $('#contact-form');
    const submit = $('#contact-submit');
    const status = $('#form-status');

    const setStatus = (type, text) => {
      status.className = 'form-status ' + type;
      status.textContent = text;
    };
    const showErrors = (errors) => {
      $$('.field', form).forEach((field) => {
        const input = $('input, textarea', field);
        const msg = $('.field-error', field);
        if (!input || !msg) return;
        const error = errors[input.name] || '';
        msg.textContent = error;
        field.classList.toggle('invalid', Boolean(error));
        input.setAttribute('aria-invalid', String(Boolean(error)));
      });
      const firstInvalid = $('.field.invalid input, .field.invalid textarea', form);
      if (firstInvalid) firstInvalid.focus();
    };

    // 입력하면 해당 필드 에러는 지운다
    form.addEventListener('input', (e) => {
      const field = e.target.closest('.field');
      if (!field || !field.classList.contains('invalid')) return;
      field.classList.remove('invalid');
      $('.field-error', field).textContent = '';
    });

    form.addEventListener('submit', async (e) => {
      e.preventDefault();
      const values = {
        name: form.name.value.trim(),
        email: form.email.value.trim(),
        message: form.message.value.trim(),
        website: form.website.value,
      };
      const errors = validateContact(values);
      showErrors(errors);
      if (Object.keys(errors).length) {
        setStatus('error', '입력값을 확인해 주세요.');
        return;
      }

      submit.disabled = true;
      submit.classList.add('loading');
      setStatus('info', '전송 중…');
      const slowTimer = setTimeout(
        () => setStatus('info', '서버를 깨우는 중이에요. 최대 1분 정도 걸릴 수 있어요…'),
        3000,
      );

      try {
        const res = await api('/api/contact', { method: 'POST', body: values });
        form.reset();
        setStatus('success', res.message || '메시지가 전송되었습니다.');
      } catch (err) {
        if (err instanceof ApiError && err.status === 400 && err.body && err.body.fieldErrors) {
          showErrors(err.body.fieldErrors);
          setStatus('error', err.body.message || '입력값을 확인해 주세요.');
        } else {
          setStatus('error', '지금은 전송할 수 없어요. 잠시 후 다시 시도하거나 이메일로 연락해 주세요.');
        }
      } finally {
        clearTimeout(slowTimer);
        submit.disabled = false;
        submit.classList.remove('loading');
      }
    });
  }

  /* =========================================================
     Init
     ========================================================= */
  $('#year').textContent = new Date().getFullYear();
  initTheme();
  initHeader();
  initReveal();
  initTyping();
  initProjects();
  initStats();
  initContact();
})();
