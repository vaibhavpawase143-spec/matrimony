package com.example.repository;

import com.example.model.Payment;
import com.example.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Repository
public interface PaymentRepository extends
        JpaRepository<Payment, Long>,
        JpaSpecificationExecutor<Payment> {
    Payment findByUser(User user);

    boolean existsByTransactionId(String transactionId);

    Optional<Payment> findByTransactionId(String transactionId);

    List<Payment> findByUserId(Long userId);

    List<Payment> findByStatus(String status);

    List<Payment> findByUserIdAndStatus(Long userId, String status);

    // ================= ADMIN QUERIES =================

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'SUCCESS' AND p.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal sumRevenueByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT HOUR(p.createdAt), COUNT(p) FROM Payment p WHERE p.status = 'SUCCESS' GROUP BY HOUR(p.createdAt)")
    Map<Integer, Long> getTransactionsByHour();
    // ==========================================
// ADMIN FETCH
// ==========================================

    @EntityGraph(attributePaths = {
            "user",
            "subscriptionPlan"
    })
    Page<Payment> findAll(
            Specification<Payment> specification,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {
            "user",
            "subscriptionPlan"
    })
    Optional<Payment> findWithDetailsById(Long id);

// ==========================================
// ADMIN STATISTICS
// ==========================================

    long countByStatus(String status);

    @Query("""
        SELECT COALESCE(SUM(p.amount),0)
        FROM Payment p
        WHERE p.status='SUCCESS'
        """)
    BigDecimal getTotalRevenue();

    // ==========================================
// DASHBOARD - MONTHLY REVENUE
// ==========================================

    @Query(value = """
        SELECT TO_CHAR(created_at, 'YYYY-MM') AS month,
               COALESCE(SUM(amount),0) AS revenue
        FROM payments
        WHERE status = 'SUCCESS'
        GROUP BY TO_CHAR(created_at, 'YYYY-MM')
        ORDER BY month
        """, nativeQuery = true)
    List<Object[]> getMonthlyRevenue();

    // ==========================================
// DASHBOARD - PAYMENT METHOD DISTRIBUTION
// ==========================================

    @Query(value = """
        SELECT payment_method,
               COUNT(*)
        FROM payments
        GROUP BY payment_method
        """, nativeQuery = true)
    List<Object[]> getPaymentMethodDistribution();

// ==========================================
// DASHBOARD - TOP PAYMENT PLANS
// ==========================================

    @Query(value = """
SELECT
    sp.id,
    sp.name,
    COUNT(p.id) AS total_transactions,
    COALESCE(SUM(p.amount),0) AS total_revenue
FROM payments p
JOIN subscription_plans sp
    ON p.plan_id = sp.id
WHERE p.status='SUCCESS'
GROUP BY sp.id, sp.name
ORDER BY total_revenue DESC
LIMIT 5
""", nativeQuery = true)
    List<Object[]> getTopPaymentPlans();
    @Query(value = """
SELECT COALESCE(SUM(amount),0)
FROM payments
WHERE status='SUCCESS'
AND created_at >= DATE_TRUNC('month', CURRENT_DATE)
""", nativeQuery = true)
    BigDecimal getCurrentMonthRevenue();

    @Query(value = """
SELECT COALESCE(SUM(amount),0)
FROM payments
WHERE status='SUCCESS'
AND created_at >= DATE_TRUNC('month', CURRENT_DATE - INTERVAL '1 month')
AND created_at < DATE_TRUNC('month', CURRENT_DATE)
""", nativeQuery = true)
    BigDecimal getPreviousMonthRevenue();
}