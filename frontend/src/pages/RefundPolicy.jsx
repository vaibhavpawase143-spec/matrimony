import { RefreshCw, CreditCard, Calendar, AlertCircle, CheckCircle, Clock, DollarSign } from "lucide-react";
import { motion } from "framer-motion";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";

const RefundPolicy = () => {
  return (
    <div className="min-h-screen bg-background">
      <Navbar />

      <div className="py-12 text-center" style={{ background: "linear-gradient(135deg, hsl(270 60% 35%), hsl(290 55% 45%), hsl(270 50% 55%))" }}>
        <RefreshCw className="inline-block h-8 w-8 text-pink-soft fill-pink-soft mb-2" />
        <h1 className="text-3xl md:text-4xl font-display font-bold text-primary-foreground mb-2">Refund Policy</h1>
        <p className="text-primary-foreground/70 text-sm max-w-lg mx-auto">Clear and transparent refund terms for Gathbandhan Matrimony members</p>
      </div>

      <div className="container mx-auto px-4 py-12 max-w-4xl">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          className="bg-card rounded-xl border border-border p-8 mb-8"
        >
          <h2 className="text-2xl font-display font-bold text-foreground mb-4">Policy Overview</h2>
          <p className="text-muted-foreground leading-relaxed mb-6">
            At Gathbandhan Matrimony, we want you to be completely satisfied with our services. 
            This Refund Policy outlines the conditions under which refunds are provided for our premium membership plans.
          </p>
          <p className="text-muted-foreground leading-relaxed">
            We offer fair and transparent refund terms while ensuring the sustainability of our platform to continue 
            providing quality matrimonial services.
          </p>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ delay: 0.1 }}
          className="bg-card rounded-xl border border-border p-8 mb-8"
        >
          <div className="flex items-center gap-3 mb-4">
            <CheckCircle className="h-6 w-6 text-primary" />
            <h2 className="text-2xl font-display font-bold text-foreground">7-Day Money-Back Guarantee</h2>
          </div>
          
          <div className="space-y-4">
            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Eligibility Criteria</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>New premium members only (first-time subscription)</li>
                <li>Request must be made within 7 days of subscription purchase</li>
                <li>Account must be in good standing (no policy violations)</li>
                <li>Less than 10% of subscription period used</li>
                <li>No previous refund requests on the account</li>
              </ul>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Refund Amount</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>100% refund for requests within 3 days of purchase</li>
                <li>75% refund for requests between 4-7 days of purchase</li>
                <li>Processing fees may apply (maximum 5% of refund amount)</li>
                <li>Refund processed within 7-10 business days</li>
              </ul>
            </div>
          </div>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ delay: 0.2 }}
          className="bg-card rounded-xl border border-border p-8 mb-8"
        >
          <div className="flex items-center gap-3 mb-4">
            <AlertCircle className="h-6 w-6 text-primary" />
            <h2 className="text-2xl font-display font-bold text-foreground">Non-Refundable Situations</h2>
          </div>
          
          <div className="space-y-4">
            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">No Refund Available For:</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Requests made after 7 days from subscription purchase</li>
                <li>Used more than 10% of subscription period</li>
                <li>Accounts with policy violations or suspensions</li>
                <li>Basic/free membership plans</li>
                <li>Profile verification or premium feature fees</li>
                <li>Change of mind after extensive usage</li>
                <li>Technical issues resolved by our support team</li>
              </ul>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Partial Refunds Not Available</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>No pro-rata refunds for unused time</li>
                <li>No refunds for auto-renewal charges (unless cancelled within 7 days)</li>
                <li>No refunds for promotional or discounted plans</li>
                <li>No refunds for gift subscriptions</li>
              </ul>
            </div>
          </div>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ delay: 0.3 }}
          className="bg-card rounded-xl border border-border p-8 mb-8"
        >
          <div className="flex items-center gap-3 mb-4">
            <CreditCard className="h-6 w-6 text-primary" />
            <h2 className="text-2xl font-display font-bold text-foreground">Payment Processing</h2>
          </div>
          
          <div className="space-y-4">
            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Refund Methods</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Refund to original payment method (credit/debit card, UPI, etc.)</li>
                <li>Bank transfer for failed original payment method refunds</li>
                <li>Wallet credit for future use (upon request)</li>
                <li>Processing time: 7-10 business days</li>
              </ul>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Processing Fees</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Payment gateway charges (up to 2.5%)</li>
                <li>Bank processing fees (if applicable)</li>
                <li>Currency conversion charges (for international payments)</li>
                <li>Maximum total fees: 5% of refund amount</li>
              </ul>
            </div>
          </div>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ delay: 0.4 }}
          className="bg-card rounded-xl border border-border p-8 mb-8"
        >
          <div className="flex items-center gap-3 mb-4">
            <Calendar className="h-6 w-6 text-primary" />
            <h2 className="text-2xl font-display font-bold text-foreground">Subscription Cancellation</h2>
          </div>
          
          <div className="space-y-4">
            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Auto-Renewal Cancellation</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Cancel anytime through account settings</li>
                <li>Access continues until end of billing period</li>
                <li>No refund for remaining time in current period</li>
                <li>Confirmation email sent upon cancellation</li>
              </ul>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Immediate Cancellation</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Available within 7 days of purchase</li>
                <li>Eligible for money-back guarantee</li>
                <li>Account access suspended immediately</li>
                <li>Refund processed according to policy terms</li>
              </ul>
            </div>
          </div>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ delay: 0.5 }}
          className="bg-card rounded-xl border border-border p-8 mb-8"
        >
          <div className="flex items-center gap-3 mb-4">
            <Clock className="h-6 w-6 text-primary" />
            <h2 className="text-2xl font-display font-bold text-foreground">Special Circumstances</h2>
          </div>
          
          <div className="space-y-4">
            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Technical Issues</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Platform-wide technical failures: Full refund</li>
                <li>Account-specific issues resolved: No refund</li>
                <li>Service unavailability for extended periods: Pro-rata refund</li>
                <li>Document all technical issues with timestamps</li>
              </ul>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Medical Emergencies</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Medical emergencies with documentation: Considered case-by-case</li>
                <li>Requires valid medical certificate</li>
                <li>Partial refund may be offered based on remaining time</li>
                <li>Management approval required</li>
              </ul>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-foreground mb-2">Marriage Success</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Successful marriage: No refund</li>
                <li>Account can be transferred to family member</li>
                <li>Remaining time gifted to relatives/friends</li>
                <li>Marriage certificate required for verification</li>
              </ul>
            </div>
          </div>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ delay: 0.6 }}
          className="bg-card rounded-xl border border-border p-8 mb-8"
        >
          <h2 className="text-2xl font-display font-bold text-foreground mb-6">How to Request a Refund</h2>
          
          <div className="space-y-6">
            <div>
              <h3 className="text-lg font-semibold text-foreground mb-3">Step 1: Contact Support</h3>
              <p className="text-muted-foreground mb-3">
                Email us at <strong>refunds@gathbandhan.com</strong> or call +91 8999823244
              </p>
              <p className="text-muted-foreground">
                Include your registered email, subscription details, and reason for refund request.
              </p>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-foreground mb-3">Step 2: Provide Documentation</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Subscription receipt or transaction ID</li>
                <li>Registered email address and phone number</li>
                <li>Reason for refund request (detailed explanation)</li>
                <li>Any supporting documents (for special circumstances)</li>
              </ul>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-foreground mb-3">Step 3: Review Process</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Support team reviews request within 48 hours</li>
                <li>Eligibility verified against refund policy</li>
                <li>Decision communicated via email</li>
                <li>Refund processed if approved</li>
              </ul>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-foreground mb-3">Step 4: Refund Processing</h3>
              <ul className="list-disc list-inside text-muted-foreground space-y-2">
                <li>Refund initiated within 24 hours of approval</li>
                <li>Processing time: 7-10 business days</li>
                <li>Confirmation email with transaction details</li>
                <li>Account access suspended immediately</li>
              </ul>
            </div>
          </div>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ delay: 0.7 }}
          className="bg-card rounded-xl border border-border p-8"
        >
          <h2 className="text-2xl font-display font-bold text-foreground mb-6">Important Notes</h2>
          
          <div className="space-y-4">
            <div className="flex items-start gap-3">
              <DollarSign className="h-5 w-5 text-primary mt-1 flex-shrink-0" />
              <div>
                <h3 className="font-semibold text-foreground mb-2">Currency & Exchange Rates</h3>
                <p className="text-muted-foreground leading-relaxed">
                  All refunds are processed in Indian Rupees (INR). For international payments, 
                  exchange rates at the time of refund processing apply, and currency conversion 
                  charges may be deducted.
                </p>
              </div>
            </div>

            <div className="flex items-start gap-3">
              <AlertCircle className="h-5 w-5 text-primary mt-1 flex-shrink-0" />
              <div>
                <h3 className="font-semibold text-foreground mb-2">Policy Changes</h3>
                <p className="text-muted-foreground leading-relaxed">
                  This refund policy may be updated periodically. Changes will be effective 
                  immediately upon posting on our website. Existing subscriptions will be 
                  governed by the policy in effect at the time of purchase.
                </p>
              </div>
            </div>

            <div className="flex items-start gap-3">
              <CheckCircle className="h-5 w-5 text-primary mt-1 flex-shrink-0" />
              <div>
                <h3 className="font-semibold text-foreground mb-2">Final Decision</h3>
                <p className="text-muted-foreground leading-relaxed">
                  Gathbandhan Matrimony reserves the right to make final decisions on 
                  all refund requests. All decisions are made in good faith and in accordance 
                  with this policy and applicable laws.
                </p>
              </div>
            </div>
          </div>

          <div className="mt-8 pt-6 border-t border-border">
            <h3 className="font-semibold text-foreground mb-3">Contact Information</h3>
            <div className="space-y-2 text-muted-foreground">
              <p><strong>Refund Department:</strong> refunds@gathbandhan.com</p>
              <p><strong>Phone:</strong> +91 8999823244</p>
              <p><strong>Address:</strong> Sangamner College, Sangamner, Maharashtra, India - 423601</p>
            </div>
            
            <p className="text-muted-foreground leading-relaxed mt-4">
              This Refund Policy was last updated on {new Date().toLocaleDateString()} and 
              is subject to change without prior notice.
            </p>
          </div>
        </motion.div>
      </div>

      <Footer />
    </div>
  );
};

export default RefundPolicy;
