# 🏢 OfficeReserveERP : 지능형 사내 자원 관리 시스템

> **"효율적인 자원 관리의 시작, 시각화된 예약과 체계적인 결재 시스템"**
> 평면도 기반의 시각적 회의실 예약과 5단계 계층형 전자결재를 결합한 사내 ERP 웹 서비스입니다.

![Java](https://img.shields.io/badge/Java-17-007396?style=flat-square&logo=java)
![JSP/Servlet](https://img.shields.io/badge/JSP%2FServlet-MVC2-F8DC75?style=flat-square)
![Oracle](https://img.shields.io/badge/Oracle-Database-F80000?style=flat-square&logo=oracle)
![JavaScript](https://img.shields.io/badge/JavaScript-AJAX-F7DF1E?style=flat-square&logo=javascript)

---

## 💡 프로젝트 개요

단순한 CRUD(생성/읽기/수정/삭제)를 넘어, **실무에서 발생하는 데이터 충돌(동시 예약) 문제**와 **수직적 조직의 결재 프로세스**를 기술적으로 해결하는 데 중점을 둔 프로젝트입니다.

- **개발 기간:** 2026.01 ~ 2026.04
- **핵심 목표:** 
  1. 사용자 친화적인 도면 기반 예약 시스템 구축
  2. 직급에 따라 동적으로 변하는 계층형 전자결재 시스템 구현
  3. 실시간 재고 연동을 통한 비품 관리 자동화

---

## 🛠 기술 스택 (Tech Stack)

### Backend
- **Java 17** : 객체 지향적 비즈니스 로직 구현
- **JSP / Servlet** : MVC2 패턴을 적용한 아키텍처 설계
- **Lombok** : DTO 보일러플레이트 코드 최소화 및 유지보수성 향상

### Database
- **Oracle / H2 DB** : 관계형 데이터베이스를 활용한 구조적 데이터 관리
- **HikariCP** : 고성능 커넥션 풀(Connection Pool) 적용으로 DB 연동 속도 및 안정성 최적화

### Frontend
- **HTML5 / CSS3 / JavaScript**
- **AJAX (Fetch API)** : 비동기 통신을 통한 실시간 예약 가능 시간 검증 및 화면 렌더링

---

## 🚀 핵심 기능 상세 (Key Features)

### 1. 🗺 평면도 기반 스마트 회의실 예약 (AJAX)
*텍스트 위주의 딱딱한 폼 대신, 실제 사무실 도면을 클릭하여 예약하는 직관적인 UI를 제공합니다.*

- **Interactive UI** : 도면(`floor4_map.png`) 위 회의실을 클릭하면 해당 방의 상세 스펙(수용 인원, 장비 유무)과 함께 예약 창으로 이동합니다.
- **실시간 중복 예약 차단** : AJAX 통신을 통해 사용자가 선택한 날짜의 '이미 예약된 시간대'를 실시간으로 조회하고, 해당 시간 버튼을 **비활성화(Disabled)** 처리하여 데이터 충돌을 원천 차단합니다.

### 2. 📝 5단계 계층형 전자결재 시스템 (Core Logic)
*수평적/수직적 조직 문화를 모두 반영할 수 있는 동적 결재 라인 알고리즘을 구현했습니다.*

- **직급별 맞춤형 워크플로우** : 로그인한 사용자의 권한 레벨(`empLevel: 1~5`)을 판별하여 결재 시작 단계를 자동으로 설정합니다.
  - **사원(1단계)** : 기안 시 2단계부터 5단계까지 순차적인 승인이 필요합니다.
  - **최고관리자(5단계)** : 기안 즉시 모든 단계가 패스되며 '대여중' 상태로 전환됩니다.
- **결재 상태 시각화** : `SIGN1~5` 컬럼을 활용하여 현재 문서를 누가 승인했는지, 어느 단계에 머물러 있는지 직관적인 대시보드 형태로 제공합니다.

### 3. 📦 실시간 연동 비품 관리 대시보드
- **재고 동기화** : 대여 결재가 최종 승인(`대여중`)되는 즉시 비품 마스터 테이블의 '잔여 수량'이 차감되며, 반납 시 복구됩니다.
- **관리자 전용 CRUD** : 관리자 권한 로그인 시 모달(Modal) 창을 통한 비품 수정 및 폐기 기능을 제공합니다.

---

## 🏗 데이터베이스 설계 (ERD Summary)

안전한 데이터 무결성 유지를 위해 4개의 핵심 테이블을 조인하여 사용합니다.

| 테이블명 | 역할 및 핵심 컬럼 |
| :--- | :--- |
| **EMPLOYEE** | 사원 정보 관리 (`empLevel`: 결재 권한 식별자) |
| **EQUIPMENT** | 비품 마스터 재고 관리 (`totalCount`, `remainCount`) |
| **RENTAL_HISTORY** | 대여 기안 이력 및 결재 상태 추적 (`approvalStep`, `sign1~5`) |
| **RESERVATION** | 회의실 예약 관리 및 시간 슬롯 점유 데이터 |

---

## 💻 핵심 코드 (Code Snippets)

<details>
<summary><b>1. 동적 결재 라인 설정 로직 (Click to Expand)</b></summary>
<br>

사용자의 직급에 따라 결재 단계를 유연하게 스킵하는 컨트롤러 로직입니다.

```java
// RentProcessController.java
int myLevel = loginEmp.getEmpLevel(); 

if (myLevel >= 5) {
    // 5등급(최고 관리자): 기안 즉시 최종 승인 처리 및 대여 시작
    dto.setStatus("대여중");
    dto.setApprovalStep(5);
    dto.setSign1(loginEmp.getEmpName());
    // ... sign5까지 본인 서명 처리
} else {
    // 1~4등급: 본인 등급 다음 단계부터 결재 시작 (승인 대기)
    dto.setStatus("승인대기");
    dto.setApprovalStep(myLevel + 1);
    
    // 본인 등급까지는 자동으로 기안자 서명 처리 완료
    if (myLevel >= 1) dto.setSign1(loginEmp.getEmpName());
    // ... myLevel에 맞춰 서명 처리
}
```
</details>

<details>
<summary><b>2. 실시간 예약 중복 방지 (AJAX)</b></summary>
<br>

비동기 통신으로 예약된 시간을 확인하고 버튼을 제어하는 프론트엔드 로직입니다.

```javascript
// reserve.jsp
resDateInput.addEventListener('change', function() {
    const selectedDate = this.value;
    
    // 서버에 선택한 날짜의 예약 완료된 시간 목록을 요청
    fetch('checkReservedTime.do?roomId=' + roomId + '&resDate=' + selectedDate)
        .then(response => response.json())
        .then(reservedTimes => {
            // 버튼 상태 초기화 후, 이미 예약된 시간과 일치하는 버튼 비활성화
            timeButtons.forEach(btn => {
                if (reservedTimes.includes(btn.getAttribute('data-time'))) {
                    btn.disabled = true;
                }
            });
        });
});
```
</details>

---

## ⚙️ 실행 방법 (How to Run)

1. 이 저장소를 로컬 컴퓨터로 클론(`Clone`)합니다.
2. `Oracle` 또는 `H2 DB`에 포함된 `sql.txt` 파일의 스키마 및 더미 데이터를 실행하여 테이블을 세팅합니다.
3. `src/main/java/com/office/util/DBConnection.java` 파일에서 본인의 환경에 맞게 DB 연결 정보를 수정합니다.
4. `Apache Tomcat (v9.0 권장)` 서버에 프로젝트를 올리고 실행합니다.
5. `http://localhost:8080/OfficeReserveERP/index.jsp` 로 접속하여 시스템을 시작합니다.

---
*개발자: [본인 이름/닉네임 입력] | 연락처: [이메일 또는 깃허브 주소]*
