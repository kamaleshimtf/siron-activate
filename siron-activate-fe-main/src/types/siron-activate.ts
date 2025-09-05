export type Capability = {
  /**
   * the technical uuid assigned to the capability
   */
  oid: string;

  /**
   * The name of the capability
   */
  name: string;
};

export type Customer = {
  /**
   * the technical uuid assigned to the customer
   */
  oid: string;

  /**
   * The name of the customer
   */
  name: string;
};

export type License = {
  /**
   * the technical uuid assigned to the license
   */
  oid: string;

  /**
   * the name of the product for which the license has been created, it needs to match an already registered application.
   */
  productName: string | null;

  /**
   * capability names assigned to the license
   */
  capabilities: string[];

  /**
   * a string id representing the customer for which the license has been created.
   */
  customerId: string;

  /**
   * the display name of the customer for which the license has been created. Usually, the value of this field is displayed in the application using the license.
   */
  customerName: string;

  /**
   * the creation date of the license
   */
  creationDate: string;

  /**
   * the expiration date of the license
   */
  expirationDate: string;

  /**
   * the email address of the contact person for the license
   */
  contactEmail: string;
};

export type AuditLog = {
  /**
   * the technical uuid assigned to the audit log
   */
  oid: string;

  /**
   * the scope on which this audit log entry applies.
   */
  scope: string;

  /**
   * the effect referred by this audit log entry.
   */
  effect: string;

  /**
   * the principal name responsible for the action described by this audit entry.
   */
  principal?: string;

  /**
   * the timestamp of the audit log entry.
   */
  timestamp: string;

  /**
   * an optional payload object containing additional information about the audit log entry.
   */
  payload?: Record<string, unknown>;
};
