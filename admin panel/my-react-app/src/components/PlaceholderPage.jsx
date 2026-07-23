import React from "react";
import Button from "./Button";

export default function PlaceholderPage({ title, description = "", actions = [], sections = [] }) {
  return (
    <div className="space-y-6">
      <div className="flex flex-col gap-4 md:flex-row md:items-end md:justify-between">
        <div>
          <h1 className="text-3xl font-semibold text-slate-900">{title}</h1>
          {description && <p className="mt-3 max-w-2xl text-sm leading-6 text-slate-600">{description}</p>}
        </div>
        <div className="flex flex-wrap gap-3">
          {actions.map((action, index) => (
            <Button key={index} variant={action.variant || "primary"} onClick={action.onClick} className={action.className || ""}>
              {action.label}
            </Button>
          ))}
        </div>
      </div>

      <div className="grid gap-6">
        {sections.map((section, index) => (
          <section key={index} className="overflow-hidden rounded-3xl border border-slate-200 bg-white p-6 shadow-sm">
            <div className="mb-5 flex flex-col gap-3 sm:flex-row sm:justify-between sm:items-center">
              <div>
                <h2 className="text-xl font-semibold text-slate-900">{section.title}</h2>
                {section.description && <p className="mt-2 text-sm text-slate-600">{section.description}</p>}
              </div>
              {section.badge && (
                <span className="rounded-full bg-slate-100 px-3 py-1 text-xs font-semibold uppercase tracking-wide text-slate-700">
                  {section.badge}
                </span>
              )}
            </div>
            {section.items && (
              <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
                {section.items.map((item, itemIndex) => (
                  <div key={itemIndex} className="rounded-3xl border border-slate-200 bg-slate-50 p-4">
                    <p className="text-sm text-slate-500">{item.label}</p>
                    <p className="mt-2 text-2xl font-semibold text-slate-900">{item.value}</p>
                  </div>
                ))}
              </div>
            )}
            {section.list && (
              <ul className="space-y-3">
                {section.list.map((row, rowIndex) => (
                  <li key={rowIndex} className="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
                    <p className="text-sm font-semibold text-slate-900">{row.title}</p>
                    <p className="mt-1 text-sm text-slate-600">{row.subtitle}</p>
                  </li>
                ))}
              </ul>
            )}
            {section.content}
          </section>
        ))}
      </div>
    </div>
  );
}
